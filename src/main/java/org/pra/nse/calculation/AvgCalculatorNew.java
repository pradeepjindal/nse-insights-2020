package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.AvgBean;
import org.pra.nse.csv.data.AvgCao;
import org.pra.nse.csv.data.CalcBean;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.GeneralDao;
import org.pra.nse.db.dao.calc.AvgCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.repository.CalcAvgRepository;
import org.pra.nse.service.TradeDateService;
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
public class AvgCalculatorNew {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCalculatorNew.class);

    private final String computeFolderName = ApCo.AVG_DIR_NAME;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final GeneralDao generalDao;
    private final AvgCalculationDao dao;
    private final CalcAvgRepository repository;
    private final DataManager dataManager;
    private final TradeDateService tradeDateService;

    public AvgCalculatorNew(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                            GeneralDao generalDao, AvgCalculationDao avgCalculationDao,
                            CalcAvgRepository calcAvgRepository,
                            DataManager dataManager,
                            TradeDateService tradeDateService) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.generalDao = generalDao;
        this.dao = avgCalculationDao;
        this.repository = calcAvgRepository;
        this.dataManager = dataManager;
        this.tradeDateService = tradeDateService;
    }

    public void calculateAndSave(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

//        String fileName = AVG_DATA_FILE_PREFIX + "-" + forDate.toString() + ApCo.DATA_FILE_EXT;
//        String toDir = ApCo.ROOT_DIR +File.separator+ computeFolderName +File.separator+ fileName;
        String computeFilePath = getComputeOutputPath(forDate);
        LOGGER.info("{} | for:{}", AVG_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(computeFilePath)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", AVG_DATA_FILE_PREFIX, computeFilePath);
            return;
        }

        LOGGER.info("{} calculating for 20 days", AVG_DATA_FILE_PREFIX);
        Map<String, List<DeliverySpikeDto>> symbolMap;
        symbolMap = dataManager.getDataBySymbol(forDate, 20);

            Map<String, AvgBean> beansMap = new HashMap<>();
            symbolMap.values().forEach( list -> {
                list.forEach( dto -> {
                    if (dto.getTradeDate().compareTo(forDate) == 0) {
                        AvgBean bean = new AvgBean();
                        bean.setSymbol(dto.getSymbol());
                        bean.setTradeDate(dto.getTradeDate());
                        beansMap.put(dto.getSymbol(), bean);
                    }
                });
            });

        loopIt(forDate, symbolMap,
                (dto, avg) -> beansMap.get(dto.getSymbol()).setAtpAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setVolAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setDelAvg20(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setFoiAvg20(avg)
        );

        LOGGER.info("{} calculating for 10 days", AVG_DATA_FILE_PREFIX);
        symbolMap = dataManager.getDataBySymbol(forDate, 10);
        loopIt(forDate, symbolMap,
                (dto, avg) -> beansMap.get(dto.getSymbol()).setAtpAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setVolAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setDelAvg10(avg),
                (dto, avg) -> beansMap.get(dto.getSymbol()).setFoiAvg10(avg)
        );
        // calculate avg for each s symbol

        //
        List<CalcBean> calcBeanList = new ArrayList<>();
        beansMap.values().forEach(bean -> calcBeanList.add(bean));
        if(CalcHelper.validateForSaving(forDate, calcBeanList, AVG_DATA_FILE_PREFIX)) {
            List<AvgBean> avgBeanList = new ArrayList<>();
            beansMap.values().forEach(bean -> avgBeanList.add(bean));
            saveToCsv(forDate, avgBeanList);
            saveToDb(forDate, avgBeanList);
        }
    }

    private void loopIt(LocalDate forDate,
                        Map<String, List<DeliverySpikeDto>> symbolDtosMap,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerAtp,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerVol,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerDel,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerFoi) {
        //List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolDtosMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        return dto.getAtp();
                    },
                    (dto, calculatedValue) -> biConsumerAtp.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getVolume();
                    },
                    (dto, calculatedValue) -> biConsumerVol.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getDelivery();
                    },
                    (dto, calculatedValue) -> biConsumerDel.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getOi();
                    },
                    (dto, calculatedValue) -> biConsumerFoi.accept(dto, calculatedValue)
            );
        });
        //return dtos_ToBeSaved;
    }

    private void calculate(LocalDate forDate, String symbol,
                            List<DeliverySpikeDto> spikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto, BigDecimal> biConsumer) {
        // calculate avg for each symbol
        //LOGGER.info("avg | for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;

        short ctr = 0;
        BigDecimal sum = BigDecimal.ZERO;
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
            ctr++;
            sum = sum.add(indicatorColumn);
        }

        if(ctr == 0) {
            LOGGER.info("avg | for symbol = {}, ctr = {}", symbol, ctr);
        }

        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        BigDecimal ten = new BigDecimal(ctr);
        BigDecimal avg;
        avg = sum.divide(ctr == 0 ? BigDecimal.ONE : ten, 2, RoundingMode.HALF_UP);
        //===========================================

        if(latestDto != null) biConsumer.accept(latestDto, avg);
        else LOGGER.warn("skipping avg, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, avg = {}", symbol, avg);
    }

    private void saveToCsv(LocalDate forDate, List<AvgBean> dtos) {
//        String fileName = AVG_DATA_FILE_PREFIX + forDate + ApCo.DATA_FILE_EXT;
//        String toPath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + fileName;
        String computeToFilePath = getComputeOutputPath(forDate);
        AvgCao.saveOverWrite(AVG_CSV_HEADER, dtos, computeToFilePath, dto -> dto.toCsvString());
        LOGGER.info("{} | saved on disk ({})", AVG_DATA_FILE_PREFIX, computeToFilePath);
    }

    private void saveToDb(LocalDate forDate, List<AvgBean> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcAvgTab tab = new CalcAvgTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setAtpAvg10Sma(dto.getAtpAvg10());
                tab.setVolAvg10Sma(dto.getVolAvg10());
                tab.setDelAvg10Sma(dto.getDelAvg10());
                tab.setOiAvg10Sma(dto.getFoiAvg10());

                tab.setAtpAvg20Sma(dto.getAtpAvg20());
                tab.setVolAvg20Sma(dto.getVolAvg20());
                tab.setDelAvg20Sma(dto.getDelAvg20());
                tab.setOiAvg20Sma(dto.getFoiAvg20());

                repository.save(tab);
            });
            LOGGER.info("{} | uploaded", AVG_DATA_FILE_PREFIX);
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", AVG_DATA_FILE_PREFIX);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", AVG_DATA_FILE_PREFIX, dataCtr, dtos.size());
        }
    }


    private String getComputeOutputPath(LocalDate forDate) {
        String computeFileName = AVG_DATA_FILE_PREFIX + forDate + ApCo.DATA_FILE_EXT;
        String computePath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + computeFileName;
        return computePath;
    }
}
