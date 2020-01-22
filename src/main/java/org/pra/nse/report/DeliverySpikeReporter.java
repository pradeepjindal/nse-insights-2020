package org.pra.nse.report;

import org.pra.nse.ApCo;
import org.pra.nse.db.dao.NseReportsDao;
import org.pra.nse.db.dto.DeliverySpikeDto;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeliverySpikeReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverySpikeReporter.class);

    private final String DSR = "DeliverySpikeReport";
    private final String DSRF = "DeliverySpikeReportFull";

    private final NseReportsDao nseReportsDao;
    private final EmailService emailService;
    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    DeliverySpikeReporter(NseReportsDao nseReportsDao,
                          EmailService emailService,
                          NseFileUtils nseFileUtils,
                          PraFileUtils praFileUtils) {
        this.nseReportsDao = nseReportsDao;
        this.emailService = emailService;
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
    }

    public void reportFromLast() {
        String str = praFileUtils.validateDownload();
        if(str == null) return;

        LocalDate forDate = DateUtils.toLocalDate(str);
        //LocalDate forDate = LocalDate.of(2020,1,2);
        reportForDate(forDate);
    }
    public void reportForDate(LocalDate forDate) {
        String fileName = DSR + "-" + forDate.toString() + ApCo.REPORTS_FILE_EXT;
        //String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.REPORTS_DIR_NAME +File.separator+ fileName;
        String toDir = ApCo.ROOT_DIR +File.separator+ ApCo.REPORTS_DIR_NAME_MANISH +File.separator+ fileName;

        LOGGER.info("{} | for:{}", DSR, forDate.toString());
        if(nseFileUtils.isFileExist(toDir)) {
            LOGGER.warn("{} already present (regeneration and email would be skipped): {}", DSR, toDir);
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
        produceOneDayReport(dbResults, toDir, latestTenDates);
        //
        //email(null, fileName, fileName, toDir);
    }

    private void produceOneDayReport(List<DeliverySpikeDto> dbResults, String toDir, List<LocalDate> latestTenDates) {
        List<String> dataLines = new ArrayList<>();
        long rowCount = dbResults.stream()
                .filter( row -> row.getTradeDate().compareTo(latestTenDates.get(0)) == 0)
                .map( filteredRow -> dataLines.add(filteredRow.toCsvString()))
                .count();

        File csvOutputFile = new File(toDir);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println("symbol,trade_date,open,high,low,close,traded_chg_prcnt,delivered_chg_prcnt,hmo_prcnt,oml_prcnt");
            dataLines.stream()
                    //.map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: {}", e);
            throw new RuntimeException(DSR + ": Could not create file");
        }
    }

    private void email(String toEmail, String subject, String text, String pathToAttachment) {
        if(nseFileUtils.isFileExist(pathToAttachment)) {
            emailService.sendAttachmentMessage("ca.manish.thakkar@gmail.com", subject, text, pathToAttachment, null);
            emailService.sendAttachmentMessage("pradeepjindal.mca@gmail.com", subject, text, pathToAttachment, null);
            emailService.sendAttachmentMessage("shweta.jindal@haldiram.com", subject, text, pathToAttachment, null);
        } else {
            LOGGER.error("skipping email: DeliverySpikeReport not found at disk");
        }
    }

}
