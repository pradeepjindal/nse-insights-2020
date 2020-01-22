package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.AvgData;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.calc.AvgCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.dto.OiSumDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.repository.CalcAvgRepository;
import org.pra.nse.util.NseFileUtils;
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

import static org.pra.nse.calculation.CalcCons.AVG_DATA_FILE_PREFIX;
import static org.pra.nse.calculation.CalcCons.AVG_CSV_HEADER;

@Component
public class AvgCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvgCalculator.class);

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final AvgCalculationDao dao;
    private final CalcAvgRepository repository;
    private final DataManager dataManager;

    public AvgCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                         AvgCalculationDao avgCalculationDao, CalcAvgRepository calcAvgRepository, DataManager dataManager) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = avgCalculationDao;
        this.repository = calcAvgRepository;
        this.dataManager = dataManager;
    }

    public void calculateAndSave(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

        String fileName = AVG_DATA_FILE_PREFIX + "-" + forDate.toString() + ApCo.DATA_FILE_EXT;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.COMPUTE_DIR_NAME +File.separator+ fileName;

        LOGGER.info("{} | for:{}", AVG_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", AVG_DATA_FILE_PREFIX, toDir);
            return;
        }

        List<OiSumDto> oiSumDtos = dao.getOiSum(forDate);
        Map<String, BigDecimal> oiSumMap = new HashMap<>();
        oiSumDtos.stream().forEach( dto-> oiSumMap.put(dto.getSymbol(), dto.getSumOi()));

        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, 10);

        // calculate mfi for each s symbol
        List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (symbol, list)  -> {
            calculate(forDate, symbol, list,
                    dto -> {
//                        LOGGER.info("dt:{}, val:{}, del:{}, oi:{}", dto.getTradeDate(), dto.getVolume, dto.getDelivery, oiSumMap.get(dto.getSymbol());
                        return dto.getVolume();
                    },
                    (dto, avg) -> dto.setVolumeAvg10(avg)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return dto.getDelivery();
                    },
                    (dto, avg) -> dto.setDeliveryAvg10(avg)
            );
            calculate(forDate, symbol, list,
                    dto -> {
                        return oiSumMap.get(dto.getSymbol());
                    },
                    (dto, avg) -> {
                        dto.setOiAvg10(avg);
                        dtos_ToBeSaved.add(dto);
                    }
            );
        });

        //
        saveToCsv(forDate, dtos_ToBeSaved);
        saveToDb(forDate, dtos_ToBeSaved);
    }

    public void calculate(LocalDate forDate,
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

//        if("EXIDEIND".equals(symbol)) {
//            LOGGER.info("");
//        }
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
            //LOGGER.info("loopDto = {}", dsDto.toFullCsvString());
            if(dsDto.getTradeDate().compareTo(forDate)  == 0) {
                latestDto = dsDto;
            }

            //if(dsDto.getTdycloseMinusYesclose().compareTo(zero) > 0)  {
            BigDecimal rsiColumn = functionSupplier.apply(dsDto);
            if(rsiColumn==null) {
                rsiColumn = BigDecimal.ZERO;
            }
            ctr++;
            sum = sum.add(rsiColumn);
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
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    public void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }

    private void saveToCsv(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtos.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("{} | saving of csv skipped, discrepancy in the data", AVG_DATA_FILE_PREFIX);
        }
        if(forDateSet.size() != 1 && forDate.compareTo(dtos.get(0).getTradeDate()) != 0) {
            LOGGER.info("avg | csv skipped, discrepancy in the data");
            return;
        }

        String fileName = AVG_DATA_FILE_PREFIX + "-" + forDate + ApCo.DATA_FILE_EXT;
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
        File file = new File(toPath);

        AvgData.saveOverWrite(AVG_CSV_HEADER, dtos, toPath, dto -> dto.toString());
    }

    private void saveToDb(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtos.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("{} | upload skipped, discrepancy in the data", AVG_DATA_FILE_PREFIX);
            return;
        }

        if(dao.dataCount(forDate) > 0) {
            LOGGER.info("{} | upload skipped, already uploaded", AVG_DATA_FILE_PREFIX);
            return;
        }

        CalcAvgTab tab = new CalcAvgTab();
        dtos.forEach( dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());

            tab.setVolumeAvg10(dto.getVolumeAvg10());
            tab.setDeliveryAvg10(dto.getDeliveryAvg10());
            tab.setOiAvg10(dto.getOiAvg10());

            repository.save(tab);
        });
    }

}