package org.pra.nse.calculation;

import org.pra.nse.ApCo;
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

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.pra.nse.calculation.CalcCons.*;

//@Component
public class AvgCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCalculator.class);

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final GeneralDao generalDao;
    private final AvgCalculationDao dao;
    private final CalcAvgRepository repository;
    private final DataManager dataManager;
    private final TradeDateService tradeDateService;

    public AvgCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
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

        String fileName = AVG_DATA_FILE_PREFIX + forDate.toString() + ApCo.DATA_FILE_EXT;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.COMPUTE_DIR_NAME +File.separator+ fileName;

        LOGGER.info("{} | for:{}", AVG_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", AVG_DATA_FILE_PREFIX, toDir);
            return;
        }

        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, 10);
        List<DeliverySpikeDto> dtos_ToBeSaved = loopIt(forDate, symbolMap, 10);
        symbolMap = dataManager.getDataBySymbol(forDate, 20);
        dtos_ToBeSaved = loopIt(forDate, symbolMap, 20);
        // calculate avg for each s symbol
//        symbolMap.forEach( (symbol, list)  -> {
//            calculate(forDate, symbol, list,
//                    dto -> {
////                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
//                        return dto.getAtp();
//                    },
//                    (dto, avg) -> dto.setAtpAvg10(avg)
//            );
//            calculate(forDate, symbol, list,
//                    dto -> {
//                        return dto.getVolume();
//                    },
//                    (dto, avg) -> dto.setVolumeAvg10(avg)
//            );
//            calculate(forDate, symbol, list,
//                    dto -> {
//                        return dto.getDelivery();
//                    },
//                    (dto, avg) -> dto.setDeliveryAvg10(avg)
//            );
//            calculate(forDate, symbol, list,
//                    dto -> {
//                        return dto.getOi();
//                    },
//                    (dto, avg) -> {
//                        dto.setOiAvg10(avg);
//                        dtos_ToBeSaved.add(dto);
//                    }
//            );
//        });

        //
//        if(CalcHelper.validateForSaving(forDate, dtos_ToBeSaved, AVG_DATA_FILE_PREFIX)) {
//            saveToCsv(forDate, dtos_ToBeSaved);
//            saveToDb(forDate, dtos_ToBeSaved);
//        }
    }

    private List<DeliverySpikeDto> loopIt(LocalDate forDate, Map<String, List<DeliverySpikeDto>> symbolMap, int forDays) {
        List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (symbol, list) -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        return dto.getAtp();
                    },
                    (dto, avg) -> dto.setAtpAvg10(avg)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getVolume();
                    },
                    (dto, avg) -> dto.setVolAvg10(avg)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getDelivery();
                    },
                    (dto, avg) -> dto.setDelAvg10(avg)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getOi();
                    },
                    (dto, avg) -> {
                        dto.setFoiAvg10(avg);
                        dtos_ToBeSaved.add(dto);
                    }
            );
        });
        return dtos_ToBeSaved;
    }

    private void calculate(LocalDate forDate,
                            String symbol,
                            List<DeliverySpikeDto> deliverySpikeDtoList,
                            Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                            BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
        // calculate avg for each symbol
        //LOGGER.info("avg | for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;

        short ctr = 0;
        BigDecimal sum = BigDecimal.ZERO;
        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
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

        if(latestDto!=null) biConsumer.accept(latestDto, avg);
        else LOGGER.warn("skipping avg, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, avg = {}", symbol, avg);
    }

    private void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }


//    private void saveToCsv(LocalDate forDate, List<DeliverySpikeDto> dtos) {
//        String fileName = AVG_DATA_FILE_PREFIX + "-" + forDate + ApCo.DATA_FILE_EXT;
//        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
//        File file = new File(toPath);
//        AvgData.saveOverWrite(AVG_CSV_HEADER, dtos, toPath, dto -> dto.toString());
//    }

    private void saveToDb(LocalDate forDate, List<DeliverySpikeDto> dtos) {
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

                repository.save(tab);
            });
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", AVG_DATA_FILE_PREFIX);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", AVG_DATA_FILE_PREFIX, dataCtr, dtos.size());
        }
    }

}
