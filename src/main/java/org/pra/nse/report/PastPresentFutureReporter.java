package org.pra.nse.report;

import org.pra.nse.ApCo;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcAvgTab;
import org.pra.nse.db.model.CalcMfiTab;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcAvgRepository;
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

import static org.pra.nse.report.ReportConstants.PPF_CSV_HEADER;
import static org.pra.nse.report.ReportConstants.PPF_10;

@Component
public class PastPresentFutureReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PastPresentFutureReporter.class);

    private final CalcRsiRepository calcRsiRepository;
    private final CalcMfiRepository calcMfiRepository;
    private final CalcAvgRepository calcAvgRepository;
    private final EmailService emailService;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    private final DataManager dataManager;

    PastPresentFutureReporter(CalcRsiRepository calcRsiRepository,
                              CalcMfiRepository calcMfiRepository,
                              CalcAvgRepository calcAvgRepository,
                              EmailService emailService,
                              NseFileUtils nseFileUtils,
                              PraFileUtils praFileUtils,
                              DataManager dataManager) {
        this.calcRsiRepository = calcRsiRepository;
        this.calcMfiRepository = calcMfiRepository;
        this.calcAvgRepository = calcAvgRepository;
        this.emailService = emailService;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dataManager = dataManager;
    }

    public void reportFromLast() {
//        String str = praFileUtils.validateDownload();
//        if(str == null) return;
//
//        LocalDate forDate = DateUtils.toLocalDate(str);
//        //LocalDate forDate = LocalDate.of(2020,1,3);
//        reportForDate(forDate, 10);
        reportFromLast(10);
    }
    public void reportFromLast(int forMinusDays) {
        String str = praFileUtils.validateDownload();
        if(str == null) return;

        LocalDate forDate = DateUtils.toLocalDate(str);
        //LocalDate forDate = LocalDate.of(2020,1,3);
        reportForDate(forDate, forMinusDays);
    }
    public void reportForDate(LocalDate forDate) {
        reportForDate(forDate, 10);
    }
    public void reportForDate(LocalDate forDate, Integer forMinusDays) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

        String report_name = PPF_10.replace("10", forMinusDays.toString());

        String fileName = report_name + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        String filePath = ApCo.ROOT_DIR + File.separator + ApCo.REPORTS_DIR_NAME_TMP + File.separator + fileName;

        LOGGER.info("{} | for:{}", report_name, forDate.toString());
        if(nseFileUtils.isFileExist(filePath)) {
            LOGGER.warn("{} already present (regeneration and email would be skipped): {}", report_name, filePath);
            return;
        }

        produceReport(forDate, forMinusDays, filePath);
    }

    private void produceReport(LocalDate forDate, int forMinusDays, String filePath) {
        // aggregate trade by symbols
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap = dataManager.getDataByTradeDateAndSymbol(forDate, forMinusDays);

        //load old Rsi
        List<CalcRsiTab> oldRsiList = calcRsiRepository.findAll();
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setTdyCloseRsi10Ema(oldRsi.getCloseRsi10Ema());
                    tdyDto.setTdyLastRsi10Ema(oldRsi.getLastRsi10Ema());
                    tdyDto.setTdyAtpRsi10Ema(oldRsi.getAtpRsi10Ema());
                        // calculating Bells
                        calculateBells2(tdyDto);
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });

        //load old Mfi
        List<CalcMfiTab> oldMfiList = calcMfiRepository.findAll();
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

        // load avg
        BigDecimal hundred = new BigDecimal(100);
        LocalDate minDate = dataManager.getMinDate(forDate, forMinusDays);
        List<CalcAvgTab> calcAvgTabs = calcAvgRepository.findAll();
        Map<String, CalcAvgTab> calcAvgMap = calcAvgTabs.stream()
                //.peek( xray -> LOGGER.info("{}", xray.getTradeDate()) )
                .filter( row -> row.getTradeDate().compareTo(minDate) == 0)
                //.peek( xray -> LOGGER.info("{}", xray) )
                //.filter( row -> filterDate(row, minDate, forDate))
                .collect(Collectors.toMap(row->row.getSymbol(), row-> row));

        Map.Entry<String, LocalDate> previousDate = new AbstractMap.SimpleEntry<>("tradeDate", LocalDate.now());
        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, forMinusDays);
        //Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, forMinusDays);
        symbolMap.entrySet().forEach( entry -> {
            previousDate.setValue(null);
            entry.getValue().forEach( dto -> {
                BigDecimal atpOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal volumeOnePercent = calcAvgMap.get(dto.getSymbol()).getVolumeAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal deliveryOnePercent = calcAvgMap.get(dto.getSymbol()).getDeliveryAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                BigDecimal oiOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                if(previousDate.getValue() == null || previousDate.getValue().isBefore(dto.getTradeDate())) {
                    previousDate.setValue(dto.getTradeDate());
                    //BigDecimal atpOnePercent = calcAvgMap.get(dto.getSymbol()).getAtpAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                    BigDecimal atpGrowth = dto.getAtp().divide(atpOnePercent, 2, RoundingMode.HALF_UP);
                        dto.setAtpGrowth10(atpGrowth);
                    //BigDecimal volumeOnePercent = calcAvgMap.get(dto.getSymbol()).getVolumeAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                    BigDecimal volumeGrowth = dto.getVolume().divide(volumeOnePercent, 2, RoundingMode.HALF_UP);
                        dto.setVolumeGrowth10(volumeGrowth);
                    //BigDecimal deliveryOnePercent = calcAvgMap.get(dto.getSymbol()).getDeliveryAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                    BigDecimal deliveryGrowth = dto.getDelivery().divide(deliveryOnePercent, 2, RoundingMode.HALF_UP);
                        dto.setDeliveryGrowth10(deliveryGrowth);
                    //BigDecimal oiOnePercent = calcAvgMap.get(dto.getSymbol()).getOiAvg10().divide(hundred, 2, RoundingMode.HALF_UP);
                    BigDecimal oiGrowth = dto.getOi().divide(oiOnePercent, 2, RoundingMode.HALF_UP);
                        dto.setOiGrowth10(oiGrowth);
                } else {
                    LOGGER.warn("unknown condition - previousDate:{}, currentDate:{}", previousDate.getValue(), dto.getTradeDate());
                }
            });
        });

        // write report
        writeReport(filePath, symbolMap);
        String str = "PPF-" +forDate+ " (" +forMinusDays+ ")";
        email(null, str, str, filePath);
    }

    private boolean filterDate(CalcAvgTab pojo, LocalDate minDate, LocalDate maxDate) {
        return pojo.getTradeDate().isAfter(minDate.minusDays(1)) && pojo.getTradeDate().isBefore(maxDate.plusDays(1));
    }

//    private void fillTheNextData(Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateSymbolMap) {
//        //Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateSymbolMap = getDataByTradeDateAndSymbol(latestDbDate, 10);
//        long ctr = dbResults.stream().filter( row -> row.getTradeDate().isAfter(tradeDates_Desc_LinkedList.get(10))
//                && row.getTradeDate().isBefore(tradeDates_Desc_LinkedList.get(0))
//        )
//                .map( filteredRow -> {
//                    LocalDate nextDate = nextDateMap.get(filteredRow.getTradeDate());
//                    DeliverySpikeDto nextDto = tradeDateSymbolMap.get(nextDate).get(filteredRow.getSymbol());
//
//                    filteredRow.setNxtCloseToOpenPercent(nextDto.getCloseToOpenPercent());
//                    filteredRow.setNxtOptoHighPrcnt(nextDto.getOthighPrcnt());
//                    filteredRow.setNxtOptoLowPrcnt(nextDto.getOtlowPrcnt());
//                    filteredRow.setNxtOptoAtpPrcnt(nextDto.getOtatpPrcnt());
//                    return true;
//                }).count();
//    }

    private void calculateBells(DeliverySpikeDto dto) {
        String signal;
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal minPlusThreshHold = new BigDecimal(0.20f);
        BigDecimal minMinusThreshHold = new BigDecimal(-0.20f);

        if(dto.getLast().compareTo(dto.getClose()) == 1) {
            BigDecimal percent = dto.getClose().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getLast().subtract(dto.getClose());
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minPlusThreshHold) > 0 ? "bullish" : "";
            dto.setClosingBell(signal);
        } else {
            BigDecimal percent = dto.getLast().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getClose().subtract(dto.getLast()).multiply(new BigDecimal(-1));
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(minMinusThreshHold) < 0 ? "bearish" : "";
            dto.setClosingBell(signal);
        }

        if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
            signal = dto.getCloseToOpenPercent().compareTo(minPlusThreshHold) > 0 ? "gapUp" : "";
            dto.setOpeningBell(signal);
        } else {
            signal = dto.getCloseToOpenPercent().compareTo(minMinusThreshHold) < 0 ? "gapDown" : "";
            dto.setOpeningBell(signal);
        }
    }

    private void calculateBells2(DeliverySpikeDto dto) {
        String signal;
        BigDecimal hundred = new BigDecimal(100);

        BigDecimal minPlusThreshHold = new BigDecimal(0.25f);
        BigDecimal plusThreshHold = new BigDecimal(0.55f);
        BigDecimal veryPlusThreshHold = new BigDecimal(0.95f);

        BigDecimal minMinusThreshHold = new BigDecimal(-0.25f);
        BigDecimal minusThreshHold = new BigDecimal(-0.55f);
        BigDecimal veryMinusThreshHold = new BigDecimal(-0.95f);

        if(dto.getLast().compareTo(dto.getClose()) == 1) {
            BigDecimal percent = dto.getClose().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getLast().subtract(dto.getClose());
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.20f)) > 0 ? "bullish" : "";
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.45f)) > 0 ? "Bullish" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.75f)) > 0 ? "BULLISH" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(0.95f)) > 0 ? "BULLISHhh" : signal;
            dto.setClosingBell(signal);
        } else {
            BigDecimal percent = dto.getLast().divide(hundred, 2, RoundingMode.HALF_UP);
            BigDecimal diff = dto.getClose().subtract(dto.getLast()).multiply(new BigDecimal(-1));
            dto.setCloseToLastPercent(diff.divide(percent, 2, RoundingMode.HALF_UP));
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.20f)) < 0 ? "bearish" : "";
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.45f)) < 0 ? "Bearish" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.75f)) < 0 ? "BEARISH" : signal;
            signal = dto.getCloseToLastPercent().compareTo(new BigDecimal(-0.95f)) < 0 ? "BEARISHhh" : signal;
            dto.setClosingBell(signal);
        }

        if(dto.getOpen().compareTo(dto.getPreviousClose()) == 1) {
            signal = dto.getCloseToOpenPercent().compareTo(minPlusThreshHold) > 0 ? "gapup" : "";
            signal = dto.getCloseToOpenPercent().compareTo(plusThreshHold) > 0 ? "GapUp" : signal;
            signal = dto.getCloseToOpenPercent().compareTo(veryPlusThreshHold) > 0 ? "GAPUP" : signal;
            dto.setOpeningBell(signal);
        } else {
            signal = dto.getCloseToOpenPercent().compareTo(minMinusThreshHold) < 0 ? "gapdown" : "";
            signal = dto.getCloseToOpenPercent().compareTo(minusThreshHold) < 0 ? "GapDown" : signal;
            signal = dto.getCloseToOpenPercent().compareTo(veryMinusThreshHold) < 0 ? "GAPDOWN" : signal;
            dto.setOpeningBell(signal);
        }
    }

//    private void writeReport(String toPath, Map<String, List<DeliverySpikeDto>> symbolMap) {
//        List<DeliverySpikeDto> lists = new LinkedList<>();
//        symbolMap.values().forEach( list -> lists.addAll(list));
//        writeReport(toPath, lists);
//    }
    private void writeReport(String toPath, Map<String, List<DeliverySpikeDto>> symbolMap) {
        List<String> keys = symbolMap.keySet().stream().collect(Collectors.toList());
        Collections.sort(keys);

        List<DeliverySpikeDto> lists = new LinkedList<>();
        keys.forEach( key -> lists.addAll(symbolMap.get(key)) );
        writeReport(toPath, lists);
    }
    private void writeReport(String toPath, List<DeliverySpikeDto> dtos) {
        if(dtos == null || dtos.size() == 0 || dtos.isEmpty()) {
            LOGGER.warn("no data to create report");
            return;
        }
        // create and collect csv lines
        List<String> csvLines = new LinkedList<>();
        dtos.forEach( dto -> csvLines.add(dto.toPpfString2()) );

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(PPF_CSV_HEADER);
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(PPF_10 + ": Could not create file");
        }
    }

    private void email(String toEmail, String subject, String text, String pathToAttachment) {
        if(nseFileUtils.isFileExist(pathToAttachment)) {
            emailService.sendAttachmentMessage("pradeepjindal.mca@gmail.com", subject, text, pathToAttachment, null);
        } else {
            LOGGER.error("skipping email: DeliverySpikeReport not found at disk");
        }
    }

}
