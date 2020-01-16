package org.pra.nse.processor;

import org.pra.nse.ApCo;
import org.pra.nse.calc.rsi.RsiCalculator;
import org.pra.nse.csv.data.RsiBean;
import org.pra.nse.csv.data.RsiData;
import org.pra.nse.db.dao.CalcRsiDao;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.email.EmailService;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;

import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CloseableIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeliverySpikeReportFull {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverySpikeReportFull.class);

    private final String DSR = "DeliverySpikeReport";
    private final String DSRF = "DeliverySpikeReportFull";

    private final CalcRsiRepository calcRsiRepository;
    private final CalcRsiDao calcRsiDao;
    private final NseReportsDao nseReportsDao;
    private final EmailService emailService;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    DeliverySpikeReportFull(CalcRsiRepository calcRsiRepository,
                            CalcRsiDao calcRsiDao, NseReportsDao nseReportsDao,
                            EmailService emailService,
                            NseFileUtils nseFileUtils,
                            PraFileUtils praFileUtils) {
        this.calcRsiRepository = calcRsiRepository;
        this.calcRsiDao = calcRsiDao;
        this.nseReportsDao = nseReportsDao;
        this.emailService = emailService;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
    }

    public void process() {
        String str = praFileUtils.validateDownload();
        if(str == null) return;

        LocalDate forDate = DateUtils.toLocalDate(str);
        //LocalDate forDate = LocalDate.of(2020,1,3);
        process(forDate);
    }
    public void process(LocalDate forDate) {
        String fileName = DSRF + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        String filePath = ApCo.ROOT_DIR + File.separator + ApCo.REPORTS_DIR_NAME_TMP + File.separator + fileName;

        LOGGER.info("{} | for:{}", DSR, forDate.toString());
        if(nseFileUtils.isFileExist(filePath)) {
            LOGGER.warn("{} already present (regeneration and email would be skipped): {}", DSRF, filePath);
            return;
        }

        //List<DeliverySpikeDto> dbResults = nseReportsDao.getDeliverySpike(forDate);
        List<DeliverySpikeDto> dbResults = nseReportsDao.getDeliverySpike();

        Set<LocalDate> tradeDates = new HashSet<>();
        dbResults.forEach( row-> {
            tradeDates.add(row.getTradeDate());
        });
        List<LocalDate> list = tradeDates.stream().collect(Collectors.toList());
        Collections.sort(list, Collections.reverseOrder());

        LocalDate dbDate = list.get(0);
//        List<LocalDate> latestTenDates = new ArrayList<>();
//        list.stream().map(dt->latestTenDates.add(dt)).limit(10).count();
        List<LocalDate> latestTenDates = list.stream().limit(10).collect(Collectors.toList());

        if(forDate.compareTo(dbDate) != 0) {
            LOGGER.warn("forDate and dbDate are not same - ABORTING");
            return;
        }

        //
        //produceTenDayReportFull(dbResults, filePath.replace(DSR, DSRF), latestTenDates);
        produceTenDayReportFull(dbResults, filePath, latestTenDates);

    }

    private void produceTenDayReportFull(List<DeliverySpikeDto> dbResults, String filePath, List<LocalDate> latestTenDates) {
        // aggregate trade by symbols
        Map<String, List<DeliverySpikeDto>> symbolMap = new HashMap<>();
        Map<LocalDate, List<DeliverySpikeDto>> dateMap = new HashMap<>();
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap = new HashMap<>();
        long rowCount = dbResults.stream()
                .filter( row -> row.getTradeDate().isAfter(latestTenDates.get(9).minusDays(1))
                        //&& row.getTradeDate().isBefore(latestTenDates.get(0).plusDays(1))
                )
                .map( filteredRow -> {
                    if(symbolMap.containsKey(filteredRow.getSymbol())) {
                        symbolMap.get(filteredRow.getSymbol()).add(filteredRow);
                    } else {
                        List<DeliverySpikeDto> list = new ArrayList<>();
                        list.add(filteredRow);
                        symbolMap.put(filteredRow.getSymbol(), list);
                    }
                    //
                    if(dateMap.containsKey(filteredRow.getTradeDate())) {
                        dateMap.get(filteredRow.getTradeDate()).add(filteredRow);
                    } else {
                        List<DeliverySpikeDto> list = new ArrayList<>();
                        list.add(filteredRow);
                        dateMap.put(filteredRow.getTradeDate(), list);
                    }
                    //
                    if(tradeDateAndSymbolWise_DoubleMap.containsKey(filteredRow.getTradeDate())) {
                        if(tradeDateAndSymbolWise_DoubleMap.get(filteredRow.getTradeDate()).containsKey(filteredRow.getSymbol())) {
                            LOGGER.warn("tradeDate-symbol | matched tradeDate {} symbol {}", filteredRow.getTradeDate(), filteredRow.getSymbol());
                        } else {
                            tradeDateAndSymbolWise_DoubleMap.get(filteredRow.getTradeDate()).put(filteredRow.getSymbol(), filteredRow);
                        }
                    } else {
                        Map<String, DeliverySpikeDto> map = new HashMap<>();
                        map.put(filteredRow.getSymbol(), filteredRow);
                        tradeDateAndSymbolWise_DoubleMap.put(filteredRow.getTradeDate(), map);
                        LOGGER.info("tradeDate-symbol | tradeDate {}", filteredRow.getTradeDate());
                    }
                    return true;
                })
                .count();

        // calculate rsi for each s symbol
        List<DeliverySpikeDto> dtoHavingRsi = new ArrayList<>();
        symbolMap.forEach( (key, val) -> {
            RsiCalculator.calculateSma(latestTenDates, key, val, dto -> dto.getTdycloseMinusYesclose(), (dto, rsi) -> dto.setTdyCloseRsi10Ema(rsi));
            RsiCalculator.calculateSma(latestTenDates, key, val, dto -> dto.getTdylastMinusYeslast(), (dto, rsi) -> dto.setTdyLastRsi10Ema(rsi));
            RsiCalculator.calculateSma(latestTenDates, key, val, dto -> dto.getTdyatpMinusYesatp(), (dto, rsi) -> {
                dto.setTdyAtpRsi10Ema(rsi);
                dtoHavingRsi.add(dto);
            });
        });

        //loadRsiOld(dateMap);
        List<CalcRsiTab> oldRsiList = calcRsiRepository.findAll();
        //List<CalcRsiTab> oldRsiList = calcRsiDao.getRsi();
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setTdyCloseRsi10Ema(oldRsi.getCloseRsi10Ema());
                    tdyDto.setTdyLastRsi10Ema(oldRsi.getLastRsi10Ema());
                    tdyDto.setTdyAtpRsi10Ema(oldRsi.getAtpRsi10Ema());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });
        //
        saveRsiToCsv(dtoHavingRsi);
        saveRsiToDb(dtoHavingRsi);
        writeReport(symbolMap, filePath);
    }

    private void load() {
        //Rsi sales = Rsi.load().fromCsvFile("C:/Users/pradeepjindal/pra-computed-data/rsi-2020-01-01.csv");
    }

    private void saveRsiToCsv(List<DeliverySpikeDto> dtoHavingRsi) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtoHavingRsi.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) return;

        //
        LocalDate forDate = dtoHavingRsi.get(0).getTradeDate();
        String fileName = "rsi-" + forDate + ".csv";
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
        File file = new File(toPath);

        //
        String rsiCsvHeader = "symbol, trade_date, OpenRsi10Ema, HighRsi10Ema, LowRsi10Ema, CloseRsi10Ema, LastRsi10Ema, AtpRsi10Ema, HlmRsi10Ema";
        RsiData.saveOverWrite(rsiCsvHeader, dtoHavingRsi, toPath, dto -> dto.toString());
    }

    private void saveRsiToDb(List<DeliverySpikeDto> dtoHavingRsi) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtoHavingRsi.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("rsi upload skipped | discrepancy in the data");
            return;
        }
        LocalDate forDate = dtoHavingRsi.get(0).getTradeDate();
        //
        if(calcRsiDao.dataCount(forDate) > 0) {
            LOGGER.info("rsi already upload to the database");
            return;
        }

        //List<CalcRsiTab> calcRsiRows = new ArrayList<>();
        CalcRsiTab calcRsiTab = new CalcRsiTab();
        dtoHavingRsi.forEach( dto -> {
            calcRsiTab.reset();
            calcRsiTab.setSymbol(dto.getSymbol());
            calcRsiTab.setTradeDate(dto.getTradeDate());
            calcRsiTab.setCloseRsi10Ema(dto.getTdyCloseRsi10Ema());
            calcRsiTab.setLastRsi10Ema(dto.getTdyLastRsi10Ema());
            calcRsiTab.setAtpRsi10Ema(dto.getTdyAtpRsi10Ema());
            //calcRsiRows.add(calcRsiTab);
            calcRsiRepository.save(calcRsiTab);
        });
    }

    private void writeReport(Map<String, List<DeliverySpikeDto>> symbolMap, String toPath) {
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        symbolMap.values().forEach( list -> list.forEach( dto -> csvLines.add(dto.toFullCsvString())));

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("symbol,trade_date," +
                    "open,high,low,close,last,atp,hlm," +
                    "open_ChgPrcnt,high_ChgPrcnt,low_ChgPrcnt,close_ChgPrcnt,last_ChgPrcnt,atp_ChgPrcnt," +
                    "traded_ChgPrcnt,delivered_ChgPrcnt,oiChgPrcnt,premium," +
                    "othighPrcnt,otlowPrcnt,otclosePrcnt,otlastPrcnt,otatpPrcnt," +
                    "tdyAtpRsi10Ema,tdyCloseRsi10Ema,tdyLastRsi10Ema");
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(DSRF + ": Could not create file");
        }
    }


}
