package org.pra.nse.report;

import org.pra.nse.ApCo;
import org.pra.nse.data.DataManager;
import org.pra.nse.db.dto.DeliverySpikeDto;
import org.pra.nse.db.model.CalcRsiTab;
import org.pra.nse.db.repository.CalcRsiRepository;
import org.pra.nse.email.EmailService;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.DirUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static org.pra.nse.report.ReportConstants.DSRF_CSV_HEADER;
import static org.pra.nse.report.ReportConstants.DSRF;

@Component
public class DeliverySpikeReporterFull {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverySpikeReporterFull.class);

    private final String outputDirName = ApCo.REPORTS_DIR_NAME_DSR;

    private final CalcRsiRepository calcRsiRepository;
    private final EmailService emailService;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    private final DataManager dataManager;

    DeliverySpikeReporterFull(CalcRsiRepository calcRsiRepository,
                              EmailService emailService,
                              NseFileUtils nseFileUtils,
                              PraFileUtils praFileUtils,
                              DataManager dataManager) {
        this.calcRsiRepository = calcRsiRepository;
        this.emailService = emailService;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.dataManager = dataManager;
        DirUtils.ensureFolder(outputDirName);
    }

    public void reportFromLast() {
        String str = praFileUtils.validateDownload();
        if(str == null) return;

        LocalDate forDate = DateUtils.toLocalDate(str);
        //LocalDate forDate = LocalDate.of(2020,1,3);
        reportForDate(forDate, 10);
    }

    public void reportForDate(LocalDate forDate, int forMinusDays) {
        LocalDate latestNseDate = praFileUtils.getLatestNseDate();
        if(forDate.isAfter(latestNseDate)) return;

        String fileName = DSRF + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        String filePath = ApCo.ROOT_DIR + File.separator + outputDirName + File.separator + fileName;

        LOGGER.info("{} | for:{}", DSRF, forDate.toString());
        if(nseFileUtils.isFileExist(filePath)) {
            LOGGER.warn("{} already present (regeneration and email would be skipped): {}", DSRF, filePath);
            return;
        }

        produceTenDayReportFull(forDate, forMinusDays, filePath);
    }

    private void produceTenDayReportFull(LocalDate forDate, int forMinusDays, String filePath) {
        // aggregate trade by symbols
        Map<String, List<DeliverySpikeDto>> symbolMap = new HashMap<>();
        Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap = dataManager.getDataByTradeDateAndSymbol(forDate, forMinusDays);

        //loadRsiOld(dateMap);
        List<CalcRsiTab> oldRsiList = calcRsiRepository.findAll();
        //List<CalcRsiTab> oldRsiList = calcRsiDao.getRsi();
        oldRsiList.forEach( oldRsi -> {
            if(tradeDateAndSymbolWise_DoubleMap.containsKey(oldRsi.getTradeDate())) {
                if(tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).containsKey(oldRsi.getSymbol())) {
                    DeliverySpikeDto tdyDto = tradeDateAndSymbolWise_DoubleMap.get(oldRsi.getTradeDate()).get(oldRsi.getSymbol());
                    tdyDto.setTdyCloseRsi10Sma(oldRsi.getCloseRsi10Sma());
                    tdyDto.setTdyLastRsi10Sma(oldRsi.getLastRsi10Sma());
                    tdyDto.setTdyAtpRsi10Sma(oldRsi.getAtpRsi10Sma());
                } else {
                    //LOGGER.warn("old rsi | symbol {} not found for tradeDate {}", oldRsi.getSymbol(), oldRsi.getTradeDate());
                }
            } else {
                //LOGGER.warn("old rsi | tradeDate {} not found for symbol {}", oldRsi.getTradeDate(), oldRsi.getSymbol());
            }
        });

        writeReport(filePath, tradeDateAndSymbolWise_DoubleMap);
    }

    private void writeReport(String toPath, Map<LocalDate, Map<String, DeliverySpikeDto>> tradeDateAndSymbolWise_DoubleMap) {
        List<DeliverySpikeDto> list = new ArrayList<>();
        tradeDateAndSymbolWise_DoubleMap.values().forEach( map -> list.addAll(map.values()));
        writeReport(toPath, list);
    }

    private void writeReport(String toPath, List<DeliverySpikeDto> dtos) {
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        dtos.forEach( dto -> csvLines.add(dto.toFullCsvString()) );

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(DSRF_CSV_HEADER);
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(DSRF + ": Could not create file");
        }
    }

    private void writeReportOriginal(Map<String, List<DeliverySpikeDto>> symbolMap, String toPath) {
        // create and collect csv lines
        List<String> csvLines = new ArrayList<>();
        symbolMap.values().forEach( list -> list.forEach( dto -> csvLines.add(dto.toFullCsvString())));

        // print csv lines
        File csvOutputFile = new File(toPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(DSRF_CSV_HEADER);
            csvLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(DSRF + ": Could not create file");
        }
    }

}
