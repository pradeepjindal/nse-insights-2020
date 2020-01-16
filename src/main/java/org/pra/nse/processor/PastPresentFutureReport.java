package org.pra.nse.processor;

import org.pra.nse.ApCo;
import org.pra.nse.calc.rsi.MfiCalculator;
import org.pra.nse.csv.data.MfiData;
import org.pra.nse.csv.data.RsiData;
import org.pra.nse.db.dao.CalcMfiDao;
import org.pra.nse.db.dao.CalcRsiDao;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcMfiRepository;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.email.EmailService;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PastPresentFutureReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(PastPresentFutureReport.class);

    private final String PPF_10 = "PPF_10_Report";
    private final String PPF_FULL = "PPF_Full_Report";

    private final CalcRsiRepository calcRsiRepository;
    private final CalcMfiRepository calcMfiRepository;
    private final CalcRsiDao calcRsiDao;
    private final CalcMfiDao calcMfiDao;
    private final NseReportsDao nseReportsDao;
    private final EmailService emailService;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    PastPresentFutureReport(CalcRsiRepository calcRsiRepository,
                            CalcMfiRepository calcMfiRepository,
                            CalcRsiDao calcRsiDao,
                            CalcMfiDao calcMfiDao,
                            NseReportsDao nseReportsDao,
                            EmailService emailService,
                            NseFileUtils nseFileUtils,
                            PraFileUtils praFileUtils) {
        this.calcRsiRepository = calcRsiRepository;
        this.calcMfiRepository = calcMfiRepository;
        this.calcRsiDao = calcRsiDao;
        this.calcMfiDao = calcMfiDao;
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
        String fileName = PPF_10 + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        String filePath = ApCo.ROOT_DIR + File.separator + ApCo.REPORTS_DIR_NAME_TMP + File.separator + fileName;

        LOGGER.info("{} | for:{}", PPF_10, forDate.toString());
        if(nseFileUtils.isFileExist(filePath)) {
            LOGGER.warn("{} already present (regeneration and email would be skipped): {}", PPF_10, filePath);
            return;
        }

        //List<DeliverySpikeDto> dbResults = nseReportsDao.getDeliverySpike();
        List<DeliverySpikeDto> dbResults = nseReportsDao.getDeliverySpike(forDate);

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
        produceTenDayReport(dbResults, filePath, latestTenDates);

    }

    private void produceTenDayReport(List<DeliverySpikeDto> dbResults, String filePath, List<LocalDate> latestTenDates) {
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
                        //LOGGER.info("tradeDate-symbol | tradeDate {}", filteredRow.getTradeDate());
                    }
                    calculateBells(filteredRow);
                    return true;
                })
                .count();

        // calculate mfi for each s symbol
        List<DeliverySpikeDto> dtoMfi_ToBeSaved = new ArrayList<>();
        symbolMap.forEach( (symbol, list)  -> {
                    MfiCalculator.calculate(latestTenDates, symbol, list,
                            dto -> {
//                        LOGGER.info("dt:{}, pm:{}, atp:{}, vol:{}, del:{}, H:{}, L:{}, C:{},L:{}",
//                                dto.getTradeDate(), dto.getAtpChgPrcnt(), dto.getAtp(), dto.getVolume(), dto.getDelivery(), dto.getHigh(), dto.getLow(), dto.getClose(), dto.getLast());
                                if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                                    return dto.getAtp().multiply(dto.getVolume());
                                } else {
                                    return dto.getAtp().multiply(dto.getVolume()).multiply(new BigDecimal(-1));
                                }
                            },
                            (dto, rsi) -> dto.setVolumeAtpMfi10(rsi));
                    MfiCalculator.calculate(latestTenDates, symbol, list,
                            dto -> {
                                if(dto.getAtpChgPrcnt().compareTo(BigDecimal.ZERO) > 0) {
                                    return dto.getAtp().multiply(dto.getDelivery());
                                } else {
                                    return dto.getAtp().multiply(dto.getDelivery()).multiply(new BigDecimal(-1));
                                }
                            },
                            (dto, mfi) -> {
                                dto.setDeliveryAtpMfi10(mfi);
                                dtoMfi_ToBeSaved.add(dto);
                            }
                    );
                });

        //load old Rsi
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
        //load old Mfi
        List<CalcMfiTab> oldMfiList = calcMfiRepository.findAll();
        //List<CalcRsiTab> oldRsiList = calcRsiDao.getRsi();
        oldMfiList.forEach( oldMfi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldMfi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).containsKey(oldMfi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldMfi.getTradeDate()).get(oldMfi.getSymbol());
                    tdyDto.setVolumeAtpMfi10(oldMfi.getVolumeAtpMfi10());
                    tdyDto.setDeliveryAtpMfi10(oldMfi.getDeliveryAtpMfi10());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });

        //
//        dtoMfi_ToBeSaved.forEach( dto -> {
//            if(dto.getLast().compareTo(dto.getClose()) == 1) {
//                dto.setClosingBell("Bullish");
//                BigDecimal bd = dto.getClose().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
//                bd = dto.getLast().divide(bd, 2, RoundingMode.HALF_UP);
//                dto.setCloseToLastPercent(bd);
//            } else {
//                dto.setClosingBell("Bearish");
//                BigDecimal bd = dto.getLast().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
//                bd = dto.getClose().divide(bd, 2, RoundingMode.HALF_UP);
//                dto.setCloseToLastPercent(bd);
//            }
//
//            if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
//                dto.setOpeningBell("GapUp");
//            } else {
//                dto.setOpeningBell("GapDn");
//            }
//        });

        saveMfiToCsv(dtoMfi_ToBeSaved);
        saveMfiToDb(dtoMfi_ToBeSaved);
        writeReport(symbolMap, filePath);
    }

    private void calculateBells(DeliverySpikeDto dto) {
        String signal;
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal minPlusThreshHold = new BigDecimal(0.20f);
        BigDecimal minMinusThreshHold = new BigDecimal(-0.20f);
        if(dto.getLast().compareTo(dto.getClose()) == 1) {
            BigDecimal percent = dto.getClose().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getLast().subtract(dto.getClose());
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minPlusThreshHold) > 0 ? "Bullish" : "";
            dto.setClosingBell(signal);
        } else {
            BigDecimal percent = dto.getLast().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getClose().subtract(dto.getLast()).multiply(new BigDecimal(-1));
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minMinusThreshHold) < 0 ? "Bearish" : "";
            dto.setClosingBell(signal);
        }

        if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
            signal = dto.getCloseToOpenPercent().compareTo(minPlusThreshHold) > 0 ? "GapUp" : "";
            dto.setOpeningBell(signal);
        } else {
            signal = dto.getCloseToOpenPercent().compareTo(minMinusThreshHold) < 0 ? "GapDown" : "";
            dto.setOpeningBell(signal);
        }
    }

    private void saveMfiToCsv(List<DeliverySpikeDto> dtoHavingMfi) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtoHavingMfi.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("mfi | csv skipped, discrepancy in the data");
            return;
        }

        //
        LocalDate forDate = dtoHavingMfi.get(0).getTradeDate();
        String fileName = "mfi-" + forDate + ".csv";
        String toPath = ApCo.ROOT_DIR + File.separator + ApCo.COMPUTE_DIR_NAME + File.separator + fileName;
        File file = new File(toPath);

        //
        String rsiCsvHeader = "symbol, trade_date, " +
                "volumeAtpMfi05, volumeAtpMfi10, volumeAtpMfi15, volumeAtpMfi20, " +
                "deliveryAtpMfi05, deliveryAtpMfi10, deliveryAtpMfi15, deliveryAtpMfi20";
        MfiData.saveOverWrite(rsiCsvHeader, dtoHavingMfi, toPath, dto -> dto.toString());
    }

    private void saveMfiToDb(List<DeliverySpikeDto> dtoHavingMfi) {
        //validateDate
        Set<LocalDate> forDateSet = new HashSet<>();
        dtoHavingMfi.forEach( dto -> forDateSet.add(dto.getTradeDate()));
        if(forDateSet.size() != 1) {
            LOGGER.info("mfi | upload skipped, discrepancy in the data");
            return;
        }
        LocalDate forDate = dtoHavingMfi.get(0).getTradeDate();
        //
        if(calcMfiDao.dataCount(forDate) > 0) {
            LOGGER.info("mfi | already upload to the database");
            return;
        }

        //List<CalcRsiTab> calcRsiRows = new ArrayList<>();
        CalcMfiTab tab = new CalcMfiTab();
        dtoHavingMfi.forEach( dto -> {
            tab.reset();
            tab.setSymbol(dto.getSymbol());
            tab.setTradeDate(dto.getTradeDate());
            tab.setVolumeAtpMfi10(dto.getVolumeAtpMfi10());
            tab.setDeliveryAtpMfi10(dto.getDeliveryAtpMfi10());
            //calcRsiRows.add(calcRsiTab);
            calcMfiRepository.save(tab);
        });
    }

    private void writeReport(Map<String, List<DeliverySpikeDto>> symbolMap, String toPath) {
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        symbolMap.values().forEach( list -> list.forEach( dto -> csvLines.add(dto.toPpfString())));

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(
                    "symbol,trade_date," +
                    "open,high,low,close,last,closingBell,closeToLastPrcnt,atp,hlm," +
                    "tradedChgPrcnt,deliveredChgPrcnt,oiChgPrcnt," +
                    "premium,openingBell," +
                    "closeToOpenPrcnt,otHighPrcnt,otLowPrcnt,otAtpPrcnt," +
                    "VolumeAtpMfi10,DeliveryAtpMfi10," +
                    "AtpRsi10Ema,CloseRsi10Ema,LastRsi10Ema"
            );
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(PPF_10 + ": Could not create file");
        }
    }

}
