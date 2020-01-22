package org.pra.nse.calculation;

import org.pra.nse.ApCo;
import org.pra.nse.csv.data.RsiData;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dao.calc.RsiCalculationDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
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

import static org.pra.nse.calculation.CalcCons.RSI_DATA_FILE_PREFIX;
import static org.pra.nse.calculation.CalcCons.RSI_CSV_HEADER;

@Component
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

        String fileName = RSI_DATA_FILE_PREFIX + "-" + forDate.toString() + ApCo.DATA_FILE_EXT;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.COMPUTE_DIR_NAME +File.separator+ fileName;

        LOGGER.info("{} | for:{}", RSI_DATA_FILE_PREFIX, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (calculation and saving would be skipped): {}", RSI_DATA_FILE_PREFIX, toDir);
            return;
        }

        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, 10);

        // calculate rsi for each s symbol
        List<DeliverySpikeDto> dtos_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (key, val) -> {
            calculateSma(forDate, key, val, dto -> dto.getTdycloseMinusYesclose(), (dto, rsi) -> dto.setTdyCloseRsi10Ema(rsi));
            calculateSma(forDate, key, val, dto -> dto.getTdylastMinusYeslast(), (dto, rsi) -> dto.setTdyLastRsi10Ema(rsi));
            calculateSma(forDate, key, val, dto -> dto.getTdyatpMinusYesatp(), (dto, rsi) -> {
                dto.setTdyAtpRsi10Ema(rsi);
                dtos_ToBeSaved.add(dto);
            });
        });

        //
        saveToCsv(forDate, dtos_ToBeSaved);
        saveToDb(forDate, dtos_ToBeSaved);
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
            BigDecimal rsiColumn = functionSupplier.apply(dsDto);
            if(rsiColumn.compareTo(zero) > 0)  {
                //up = up.add(dsDto.getTdycloseMinusYesclose());
                up = up.add(rsiColumn);
                upCtr++;
            }
            else {
                //dn = dn.add(dsDto.getTdycloseMinusYesclose());
                dn = dn.add(rsiColumn);
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

    private void saveToCsv(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtos.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) return;

        String fileName = RSI_DATA_FILE_PREFIX + "-" + forDate + ApCo.DATA_FILE_EXT;
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
        File file = new File(toPath);

        RsiData.saveOverWrite(RSI_CSV_HEADER, dtos, toPath, dto -> dto.toString());
    }

    private void saveToDb(LocalDate forDate, List<DeliverySpikeDto> dtos) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtos.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("{} | saving of csv skipped, discrepancy in the data", RSI_DATA_FILE_PREFIX);
        }
        if(forDateSet.size() != 1 && forDate.compareTo(dtos.get(0).getTradeDate()) != 0) {
            LOGGER.info("{} | upload skipped, discrepancy in the data", RSI_DATA_FILE_PREFIX);
            return;
        }

        if(dao.dataCount(forDate) > 0) {
            LOGGER.info("{} | upload skipped, already uploaded", RSI_DATA_FILE_PREFIX);
            return;
        }

        CalcRsiTab calcRsiTab = new CalcRsiTab();
        dtos.forEach( dto -> {
            calcRsiTab.reset();
            calcRsiTab.setSymbol(dto.getSymbol());
            calcRsiTab.setTradeDate(dto.getTradeDate());

            calcRsiTab.setCloseRsi10Ema(dto.getTdyCloseRsi10Ema());
            calcRsiTab.setLastRsi10Ema(dto.getTdyLastRsi10Ema());
            calcRsiTab.setAtpRsi10Ema(dto.getTdyAtpRsi10Ema());

            repository.save(calcRsiTab);
        });
    }

}