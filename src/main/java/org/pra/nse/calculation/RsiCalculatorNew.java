package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.*;
import org.pra.nse.service.DataService;
import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.NumberUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class RsiCalculatorNew {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsiCalculatorNew.class);

    private final String calc_name = CalcCons.RSI_FILE_PREFIX;
    private final String csv_header = CalcCons.RSI_CSV_HEADER;

    private final String computeFolderName = ApCo.RSI_DIR_NAME;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final RsiCalculationDao dao;
    private final CalcRsiRepository repository;
    private final DataService dataService;

    RsiCalculatorNew(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                     RsiCalculationDao rsiCalculationDao, CalcRsiRepository calcRsiRepository, DataService dataService) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = rsiCalculationDao;
        this.repository = calcRsiRepository;
        this.dataService = dataService;
    }


    public List<RsiBean> calculateAndReturn(LocalDate forDate) {
        Map<String, RsiBean> beansMap = prepareData(forDate);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<RsiBean> rsiBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            rsiBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            return rsiBeanList;
        } else {
            return Collections.emptyList();
        }
    }

    public void calculateAndSave(LocalDate forDate) {
        String computeFilePath = getComputeOutputPath(forDate);
        LOGGER.info("{} | for:{}", calc_name, forDate.toString());
        if(nseFileUtils.isFileExist(computeFilePath)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", calc_name, computeFilePath);
            return;
        }

        Map<String, RsiBean> beansMap = prepareData(forDate);
        List<CalcBean> calcBeanList = new ArrayList<>();
        List<RsiBean> rsiBeanList = new ArrayList<>();
        beansMap.values().forEach( bean -> {
            calcBeanList.add(bean);
            rsiBeanList.add(bean);
        });
        if(CalcHelper.validateForSaving(forDate, calcBeanList, calc_name)) {
            saveToCsv(forDate, rsiBeanList);
            //saveToDb(forDate, rsiBeanList);
        }
    }


    private Map<String, RsiBean> prepareData(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return Collections.emptyMap();

        LOGGER.info("{} calculating for 20 days", calc_name);
        Map<String, List<DeliverySpikeDto>> symbolMap;
        symbolMap = dataService.getRawDataBySymbol(forDate, 20);

        Map<String, RsiBean> beansMap = new HashMap<>();
        symbolMap.values().forEach( list -> {
            list.forEach( dto -> {
                if (dto.getTradeDate().compareTo(forDate) == 0) {
                    RsiBean bean = new RsiBean();
                    bean.setSymbol(dto.getSymbol());
                    bean.setTradeDate(dto.getTradeDate());
                    beansMap.put(dto.getSymbol(), bean);
                }
            });
        });

        loopIt(forDate, symbolMap,
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setAtpRsi20(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setCloseRsi20(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setLastRsi20(rsi)
        );

        LOGGER.info("{} calculating for 10 days", calc_name);
        symbolMap = dataService.getRawDataBySymbol(forDate, 10);
        loopIt(forDate, symbolMap,
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setAtpRsi10(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setCloseRsi10(rsi),
                (dto, rsi) -> beansMap.get(dto.getSymbol()).setLastRsi10(rsi)
        );

        return beansMap;
    }

    private void loopIt(LocalDate forDate,
                        Map<String, List<DeliverySpikeDto>> symbolDtosMap,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerAtp,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerClose,
                        BiConsumer<DeliverySpikeDto, BigDecimal> biConsumerLast) {
        //List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolDtosMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        return dto.getTdyatpMinusYesatp();
                    },
                    (dto, calculatedValue) -> biConsumerAtp.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getTdycloseMinusYesclose();
                    },
                    (dto, calculatedValue) -> biConsumerClose.accept(dto, calculatedValue)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getTdylastMinusYeslast();
                    },
                    (dto, calculatedValue) -> biConsumerLast.accept(dto, calculatedValue)
            );
        });
        //return dtos_ToBeSaved;
    }

    public void calculate(LocalDate forDate, String symbol,
                            List<DeliverySpikeDto> spikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
//        if(spikeDtoList.size() != 10) {
//            LOGGER.warn("size of the dto list is not 10, it is {}, for {}", spikeDtoList.size(), spikeDtoList.get(0).getSymbol());
//        }
        // calculate rsi for each s symbol
        //LOGGER.info("for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal up = BigDecimal.ZERO;
        short upCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;
        short dnCtr = 0;
        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:spikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal indicatorColumn = functionSupplier.apply(dsDto);
            if(indicatorColumn.compareTo(zero) > 0)  {
                //up = up.add(dsDto.getTdycloseMinusYesclose());
                up = up.add(indicatorColumn);
                upCtr++;
            }
            else {
                //dn = dn.add(dsDto.getTdycloseMinusYesclose());
                dn = dn.add(indicatorColumn);
                dnCtr++;
            }
        }
        //LOGGER.info("latestDto = {}", latestDto.toFullCsvString());
        up = up.divide(upCtr == 0 ? BigDecimal.ONE : new BigDecimal(upCtr), 2, RoundingMode.HALF_UP);
        dn = dn.divide(dnCtr == 0 ? BigDecimal.ONE : new BigDecimal(dnCtr), 2, RoundingMode.HALF_UP);

        BigDecimal rs = BigDecimal.ZERO;
        if(dn.abs().compareTo(BigDecimal.ZERO) == 0) {
            LOGGER.warn("rsi | {}, all closing are up", symbol);
            rs = up.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
        } else {
            rs = up.divide(dn.abs(), 2, RoundingMode.HALF_UP);
        }


        //rsi = 100 - (100 / (1 + rs));
        //------------------------------------------
        //(1 + rs)
        BigDecimal rsi = rs.add(BigDecimal.ONE);
        //(100 / (1 + rs)
        rsi = NumberUtils.HUNDRED.divide(rsi, 2, RoundingMode.HALF_UP);
        //100 - (100 / (1 + rs))
        rsi = NumberUtils.HUNDRED.subtract(rsi);
        //===========================================

        if(latestDto != null) biConsumer.accept(latestDto, rsi);
        else LOGGER.warn("skipping rsi, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    private void saveToCsv(LocalDate forDate, List<RsiBean> dtos) {
        String computeToFilePath = getComputeOutputPath(forDate);
        RsiCao.saveOverWrite(csv_header, dtos, computeToFilePath, dto -> dto.toCsvString());
        LOGGER.info("{} | saved on disk ({})", calc_name, computeToFilePath);
    }

    private void saveToDb(LocalDate forDate, List<RsiBean> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcRsiTab tab = new CalcRsiTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setCloseRsi10Sma(dto.getCloseRsi10());
                tab.setLastRsi10Sma(dto.getLastRsi10());
                tab.setAtpRsi10Sma(dto.getAtpRsi10());

                tab.setCloseRsi20Sma(dto.getCloseRsi20());
                tab.setLastRsi20Sma(dto.getLastRsi20());
                tab.setAtpRsi20Sma(dto.getAtpRsi20());

                repository.save(tab);
            });
            LOGGER.info("{} | uploaded", calc_name);
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", calc_name);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", calc_name, dataCtr, dtos.size());
        }
    }

    private String getComputeOutputPath(LocalDate forDate) {
        String computeFileName = calc_name + forDate + ApCo.DATA_FILE_EXT;
        String computePath = ApCo.ROOT_DIR + File.separator + computeFolderName + File.separator + computeFileName;
        return computePath;
    }

}
