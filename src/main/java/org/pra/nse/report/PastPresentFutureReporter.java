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
import org.pra.nse.util.DirUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.pra.nse.report.ReportConstants.PPF_CSV_HEADER;
import static org.pra.nse.report.ReportConstants.PPF_FULL;

@Component
public class PastPresentFutureReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PastPresentFutureReporter.class);

    private final String outputDirName = ApCo.REPORTS_DIR_NAME_PPF;

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
        DirUtils.ensureFolder(outputDirName);
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

        String report_name = PPF_FULL.replace("days", forMinusDays.toString());

        String fileName = report_name + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        String filePath = ApCo.ROOT_DIR + File.separator + outputDirName + File.separator + fileName;

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
        ReportHelper.enrichRsi(oldRsiList, tradeDateAndSymbolWise_DoubleMap);

        //load old Mfi
        List<CalcMfiTab> oldMfiList = calcMfiRepository.findAll();
        ReportHelper.enrichMfi(oldMfiList, tradeDateAndSymbolWise_DoubleMap);

        // load avg
        LocalDate minDate = dataManager.getMinDate(forDate, forMinusDays);
        List<CalcAvgTab> calcAvgTabs = calcAvgRepository.findAll();
        Map<String, CalcAvgTab> calcAvgMap = calcAvgTabs.stream()
                .filter( row -> row.getTradeDate().compareTo(minDate) == 0)
                .collect(Collectors.toMap(row->row.getSymbol(), row-> row));
        Map<String, List<DeliverySpikeDto>> symbolMap = dataManager.getDataBySymbol(forDate, forMinusDays);
        //ReportHelper.enrichGrowth10(calcAvgMap, symbolMap);
        switch (forMinusDays) {
            case 10: ReportHelper.enrichGrowth10(calcAvgMap, symbolMap); break;
            case 20: ReportHelper.enrichGrowth20(calcAvgMap, symbolMap); break;
            default:
                String errMsg = "enrichGrowth | error - forMinusDays can be either 10 or 20 only (provided forMinusDays:" +forMinusDays+ ")";
                LOGGER.error(errMsg);
                throw new RuntimeException(errMsg);
        }


        // write report
        writeReport(filePath, symbolMap);
        String str = "PPF-" +forDate+ " (" +forMinusDays+ ")";
        email(null, str, str, filePath);
    }

    private boolean filterDate(CalcAvgTab pojo, LocalDate minDate, LocalDate maxDate) {
        return pojo.getTradeDate().isAfter(minDate.minusDays(1)) && pojo.getTradeDate().isBefore(maxDate.plusDays(1));
    }

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
            throw new RuntimeException(PPF_FULL + ": Could not create file");
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
