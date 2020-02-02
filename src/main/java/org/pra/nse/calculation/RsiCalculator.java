package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
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
public class RsiCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsiCalculator.class);

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;
    private final RsiCalculationDao dao;
    private final CalcRsiRepository repository;
    private final DataManager dataManager;

    RsiCalculator(NseFileUtils nseFileUtils, PraFileUtils praFileUtils,
                  RsiCalculationDao rsiCalculationDao, CalcRsiRepository calcRsiRepository, DataManager dataManager) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dao = rsiCalculationDao;
        this.repository = calcRsiRepository;
        this.dataManager = dataManager;
    }


    public void calculateAndSave(LocalDate forDate) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

        String fileName = RSI_DATA_FILE_PREFIX + forDate.toString() + ApCo.DATA_FILE_EXT;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.COMPUTE_DIR_NAME +File.separator+ fileName;

        LOGGER.info("{} | for:{}", RSI_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", RSI_DATA_FILE_PREFIX, toDir);
            return;
        }

        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, 10);

        // calculate rsi for each s symbol
        List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (key, list) -> {
            calculateSma(forDate, key, list, dto -> dto.getTdycloseMinusYesclose(), (dto, rsi) -> dto.setTdyCloseRsi10Sma(rsi));
            calculateSma(forDate, key, list, dto -> dto.getTdylastMinusYeslast(), (dto, rsi) -> dto.setTdyLastRsi10Sma(rsi));
            calculateSma(forDate, key, list, dto -> dto.getTdyatpMinusYesatp(), (dto, rsi) -> { dto.setTdyAtpRsi10Sma(rsi);
                dtos_ToBeSaved.add(dto);
            });
        });

        //
//        if(CalcHelper.validateForSaving(forDate, dtos_ToBeSaved, RSI_DATA_FILE_PREFIX)) {
//            saveToCsv(forDate, dtos_ToBeSaved);
//            saveToDb(forDate, dtos_ToBeSaved);
//        }
    }

    public void calculateSma(LocalDate forDate,
                                String symbol,
                                List<DeliverySpikeDto> deliverySpikeDtoList,
                                Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {
        if(deliverySpikeDtoList.size() != 10) {
            LOGGER.warn("size of the dto list is not 10, it is {}, for {}", deliverySpikeDtoList.size(), deliverySpikeDtoList.get(0).getSymbol());
        }
        // calculate rsi for each s symbol
        //LOGGER.info("for symbol = {}", symbol);
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal up = BigDecimal.ZERO;
        short upCtr = 0;
        BigDecimal dn = BigDecimal.ZERO;
        short dnCtr = 0;
        DeliverySpikeDto latestDto = null;
        for(DeliverySpikeDto dsDto:deliverySpikeDtoList) {
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
        BigDecimal hundred = new BigDecimal(100);
        rsi = hundred.divide(rsi, 2, RoundingMode.HALF_UP);
        //100 - (100 / (1 + rs))
        rsi = hundred.subtract(rsi);
        //===========================================

        //if(latestDto!=null) latestDto.setTdyCloseRsi10Ema(rsi);
        if(latestDto!=null) biConsumer.accept(latestDto, rsi);
        else LOGGER.warn("skipping rsi, latestDto is null for symbol {}, may be phasing out from FnO", symbol);
        //LOGGER.info("for symbol = {}, rsi = {}", symbol, rsi);
    }

    public void calculateEma(List<LocalDate> latestTenDates,
                                    String symbol,
                                    List<DeliverySpikeDto> deliverySpikeDtoList,
                                    Function<DeliverySpikeDto, BigDecimal> functionSupplier,
                                    BiConsumer<DeliverySpikeDto,BigDecimal> biConsumer) {

    }

//    private void saveToCsv(LocalDate forDate, List<DeliverySpikeDto> dtos) {
//        String fileName = RSI_DATA_FILE_PREFIX + "-" + forDate + ApCo.DATA_FILE_EXT;
//        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
//        File file = new File(toPath);
//        RsiData.saveOverWrite(RSI_CSV_HEADER, dtos, toPath, dto -> dto.toString());
//    }

    private void saveToDb(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        long dataCtr = dao.dataCount(forDate);
        if (dataCtr == 0) {
            CalcRsiTab tab = new CalcRsiTab();
            dtos.forEach(dto -> {
                tab.reset();
                tab.setSymbol(dto.getSymbol());
                tab.setTradeDate(dto.getTradeDate());

                tab.setCloseRsi10Sma(dto.getTdyCloseRsi10Sma());
                tab.setLastRsi10Sma(dto.getTdyLastRsi10Sma());
                tab.setAtpRsi10Sma(dto.getTdyAtpRsi10Sma());

                repository.save(tab);
            });
        } else if (dataCtr == dtos.size()) {
            LOGGER.info("{} | upload skipped, already uploaded", RSI_DATA_FILE_PREFIX);
        } else {
            LOGGER.warn("{} | upload skipped, discrepancy in data dbRecords={}, dtoSize={}", RSI_DATA_FILE_PREFIX, dataCtr, dtos.size());
        }
    }

}
