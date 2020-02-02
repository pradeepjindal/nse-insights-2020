package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.CalcBean;
import org.pra.nse.csv.data.MfiBean;
import org.pra.nse.csv.data.MfiCao;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.calc.MfiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.repository.CalcMfiRepository;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.pra.nse.calculation.CalcCons.*;

@Component
public class MfiCalculatorNew {
    private static final Logger LOGGER = LoggerFactory.getLogger(MfiCalculatorNew.class);

    private final String computeFolderName = ApCo.MFI_DIR_NAME;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final MfiCalculationDao dao;
    private final CalcMfiRepository repository;
    private final DataManager dataManager;

    public MfiCalculatorNew(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                            MfiCalculationDao mfiCalculationDao, CalcMfiRepository calcMfiRepository, DataManager dataManager) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = mfiCalculationDao;
        this.repository = calcMfiRepository;
        this.dataManager = dataManager;
    }

    public void calculateAndSave(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

//        String fileName = MFI_DATA_FILE_PREFIX + "-" + forDate.toString() + ApCo.DATA_FILE_EXT;
//        String toDir = ApCo.ROOT_DIR +File.separator+ computeFolderName +File.separator+ fileName;
        String computeFilePath = getComputeOutputPath(forDate);
        LOGGER.info("{} | for:{}", MFI_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(computeFilePath)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", MFI_DATA_FILE_PREFIX, computeFilePath);
            return;
        }

        LOGGER.info("{} calculating for 20 days", MFI_DATA_FILE_PREFIX);
        Map<String, List<DeliverySpikeDto>> symbolMap;
        symbolMap = dataManager.getDataBySymbol(forDate, 20);

            Map<String, MfiBean> beansMap = new HashMap<>();
            symbolMap.values().forEach( list -> {
                list.forEach( dto -> {
                    if (dto.getTradeDate().compareTo(forDate) == 0) {
                        MfiBean bean = new MfiBean();
                        bean.setSymbol(dto.getSymbol());
                        bean.setTradeDate(dto.getTradeDate());
                        beansMap.put(dto.getSymbol(), bean);
                    }
                });
            });

        loopIt(forDate, symbolMap,
                (dto, mfi) -> beansMap.get(dto.getSymbol()).setVolMfi20(mfi),
                (dto, mfi) -> beansMap.get(dto.getSymbol()).setDelMfi20(mfi)
        );

        LOGGER.info("{} calculating for 10 days", MFI_DATA_FILE_PREFIX);
        symbolMap = dataManager.getDataBySymbol(forDate, 10);
        loopIt(forDate, symbolMap,
                (dto, mfi) -> beansMap.get(dto.getSymbol()).setVolMfi10(mfi),
                (dto, mfi) -> beansMap.get(dto.getSymbol()).setDelMfi10(mfi)
        );

        //
        List<CalcBean> calcBeanList = new ArrayList<>();
        beansMap.values().forEach(bean -> calcBeanList.add(bean));
        if(CalcHelper.validateForSaving(forDate, calcBeanList, MFI_DATA_FILE_PREFIX)) {
            List<MfiBean> beansList = new ArrayList<>();
            beansMap.values().forEach(bean -> beansList.add(bean));
            saveToCsv(forDate, beansList);
            saveToDb(forDate, beansList);
        }
    }

    private void loopIt(LocalDate forDate,
                        Map<String, List<DeliverySpikeDto>> symbolDtosMap,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerVol,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerDel) {
        //List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolDtosMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        //return dto.getVolume();
                        if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                            return dto.getAtp().multiply(dto.getVolume());
                        } else {
                            return dto.getAtp().multiply(dto.getVolume()).multiply(new BigDecimal(-1));
                        }
                    },
                    (dto, calculatedValue) -> biConsumerVol.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        //return dto.getDelivery();
                        if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                            return dto.getAtp().multiply(dto.getDelivery());
                        } else {
                            return dto.getAtp().multiply(dto.getDelivery()).multiply(new BigDecimal(-1));
                        }
                    },
                    (dto, calculatedValue) -> biConsumerDel.accept(dto, calculatedValue)
            );
        });
        //return dtos_ToBeSaved;
    }

    public void calculate(LocalDate forDate, String symbol,
                            List<DeliverySpikeDto> spikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto, BigDecimal> biConsumer) {
        // calculate mfi for each s symbol
        //LOGGER.info("mfi | for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;

        short upCtr = 0;
        BigDecimal up = BigDecimal.ZERO;
        short dnCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;

        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:spikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal indicatorColumn = functionSupplier.apply(dsDto);
            if(indicatorColumn==null) {
                indicatorColumn = BigDecimal.ZERO;
            }
            if(indicatorColumn.compareTo(zero) > 0)  {
                //up = up.add(dsDto.getTdycloseMinusYesclose());
                up = up.add(indicatorColumn);
                upCtr++;
            } else {
                //dn = dn.add(dsDto.getTdycloseMinusYesclose());
                dn = dn.add(indicatorColumn.abs());
                dnCtr++;
            }
        }

        if(upCtr == 0 || dnCtr == 0) {
            LOGGER.info("mfi | for symbol = {}, upCtr = {}, dnCtr = {}", symbol, upCtr, dnCtr);
        }

        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        BigDecimal moneyFlowRatio;
        moneyFlowRatio = up.divide(dnCtr == 0 ? BigDecimal.ONE : dn, 2, RoundingMode.HALF_UP);
        if(upCtr > 0 && dnCtr > 0) {
            moneyFlowRatio = up.divide(dn, 2, RoundingMode.HALF_UP);
        } else if(upCtr > 0 && dnCtr == 0) {
            moneyFlowRatio = up;
        } else if(upCtr == 0 && dnCtr > 0) {
            moneyFlowRatio = BigDecimal.ZERO;
        } else {
            moneyFlowRatio = BigDecimal.ZERO;
        }
        //mfi = 100 - (100 / (1 + moneyFlowRatio));
        //------------------------------------------
        //(1 + rs)
        BigDecimal mfi = moneyFlowRatio.add(BigDecimal.ONE);
        //(100 / (1 + rs)
        BigDecimal hundred = new BigDecimal(100);
        mfi = hundred.divide(mfi, 2, RoundingMode.HALF_UP);
        //100 - (100 / (1 + rs))
        mfi = hundred.subtract(mfi);
        //===========================================

        if(latestDto != null) biConsumer.accept(latestDto, mfi);
        else LOGGER.warn("skipping mfi, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, mfi = {}", symbol, mfi);
    }

    private void saveToCsv(LocalDate forDate, List<MfiBean> dtos) {
//        String fileName = MFI_DATA_FILE_PREFIX + forDate + ApCo.DATA_FILE_EXT;
//        String toPath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + fileName;
        String computeToFilePath = getComputeOutputPath(forDate);
        MfiCao.saveOverWrite(MFI_CSV_HEADER, dtos, computeToFilePath, dto -> dto.toCsvString());
        LOGGER.info("{} | saved on disk ({})", MFI_DATA_FILE_PREFIX, computeToFilePath);
    }

    private void saveToDb(LocalDate forDate, List<MfiBean> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcMfiTab tab = new CalcMfiTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setVolAtpMfi10Sma(dto.getVolMfi10());
                tab.setDelAtpMfi10Sma(dto.getDelMfi10());

                tab.setVolAtpMfi20Sma(dto.getVolMfi20());
                tab.setDelAtpMfi20Sma(dto.getDelMfi20());

                repository.save(tab);
            });
            LOGGER.info("{} | uploaded", MFI_DATA_FILE_PREFIX);
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", MFI_DATA_FILE_PREFIX);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", MFI_DATA_FILE_PREFIX, dataCtr, dtos.size());
        }
    }


    private String getComputeOutputPath(LocalDate forDate) {
        String computeFileName = MFI_DATA_FILE_PREFIX + forDate + ApCo.DATA_FILE_EXT;
        String computePath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + computeFileName;
        return computePath;
    }
}
