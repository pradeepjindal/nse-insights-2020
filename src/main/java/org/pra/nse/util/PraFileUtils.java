package org.pra.nse.util;


import org.pra.nse.ApCo;
import org.pra.nse.NseCo;
import org.pra.nse.ProCo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class PraFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PraFileUtils.class);


    public String getLatestFileNameFor(String fileDir, String filePrefix, String fileExt, int occurrence) {
        return getLatestFileNameFor(fileDir, filePrefix, fileExt, occurrence, LocalDate.now());
    }
    public String getLatestFileNameFor(String fileDir, String filePrefix, String fileExt, int occurrence, LocalDate searchBackFromThisDate) {
        return getLatestFileNameFor(fileDir, filePrefix, fileExt, occurrence, searchBackFromThisDate, ApCo.DOWNLOAD_FROM_DATE, ApCo.PRA_DTF);
    }
    public String getLatestFileNameFor(String fileDir, String filePrefix, String fileExt, int fileOccurrence,
                                       LocalDate searchBackFromThisDate, LocalDate toThisDate, DateTimeFormatter fileNameDtf) {
        LocalDate rollingDate = searchBackFromThisDate;
        File file;
        String fileName;
        String filePathWithFileName = null;
        for(int i=0; i<fileOccurrence; i++) {
            do {
                fileName = filePrefix + fileNameDtf.format(rollingDate) + fileExt;
                //LOGGER.info("getLatestFileName | fileName: {}", fileName);
                filePathWithFileName = fileDir + File.separator + fileName;
                //LOGGER.info("getLatestFileName | filePathWithFileName: {}", filePathWithFileName);
                rollingDate = rollingDate.minusDays(1);
                file = new File(filePathWithFileName);
                if(file.exists()) break;
                filePathWithFileName = null;
                if(rollingDate.compareTo(toThisDate.minusDays(5)) < 0) break;
            } while(true);
        }
        return filePathWithFileName;
    }

//    public String getLatestFileNameFor(String fileDir, String filePrefix, String fileExt, int occurrence,
//                                       LocalDate searchBackFromThisDate, LocalDate toThisDate,
//                                       DateTimeFormatter fileNameDtf) {
//        LocalDate rollingDate = searchBackFromThisDate;
//        File file;
//        String fileName = null;
//        String filePathWithFileName = null;
//        for(int i=0; i<occurrence; i++) {
//            do {
//                fileName = filePrefix + fileNameDtf.format(rollingDate) + fileExt;
//                //LOGGER.info("getLatestFileName | fileName: {}", fileName);
//                filePathWithFileName = fileDir + File.separator + fileName;
//                //LOGGER.info("getLatestFileName | filePathWithFileName: {}", filePathWithFileName);
//                rollingDate = rollingDate.minusDays(1);
//                file = new File(filePathWithFileName);
//                if(file.exists()) break;
//                filePathWithFileName = null;
//                //if(rollingDate.compareTo(ApCo.DOWNLOAD_FROM_DATE.minusDays(5)) < 0) break;
//                if(rollingDate.compareTo(toThisDate.minusDays(5)) < 0) break;
//            } while(true);
//        }
//        return filePathWithFileName;
//    }

    private String dateToString(LocalDate localDate) {
        DateTimeFormatter dtf = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                //.parseCaseInsensitive()
                // add pattern
                .appendPattern("yyyy-MM-dd")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        return LocalDate.parse(localDate.toString(), dtf).toString();
    }

    public LocalDate getLatestNseDate() {
        String cmFileName = getLatestFileNameFor(NseCo.CM_FILES_PATH, ApCo.PRA_CM_FILE_PREFIX, ApCo.DATA_FILE_EXT, 1);
        String cmDateStr = ProCo.extractDate(cmFileName);
        LocalDate cmDate = DateUtils.toLocalDate(cmDateStr);

        String fmFileName = getLatestFileNameFor(NseCo.FM_FILES_PATH, ApCo.PRA_FM_FILE_PREFIX, ApCo.DATA_FILE_EXT, 1);
        String fmDateStr = ProCo.extractDate(fmFileName);
        LocalDate fmDate = DateUtils.toLocalDate(fmDateStr);

        String dmFileName = getLatestFileNameFor(NseCo.DM_FILES_PATH, ApCo.PRA_DM_FILE_PREFIX, ApCo.DATA_FILE_EXT, 1);
        String dmDateStr = ProCo.extractDate(dmFileName);
        LocalDate dmDate = DateUtils.toLocalDate(dmDateStr);

        List<LocalDate> dates = new ArrayList<>();
        dates.add(cmDate);
        dates.add(fmDate);
        dates.add(dmDate);
        Collections.sort(dates);
        return dates.get(0);
    }

    public String validateDownload() {
        String cmDate = getLatestFileNameFor(NseCo.CM_FILES_PATH, ApCo.PRA_CM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        cmDate = ProCo.extractDate(cmDate);

        String foDate = getLatestFileNameFor(NseCo.FM_FILES_PATH, ApCo.PRA_FM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        foDate = ProCo.extractDate(foDate);

        String mtDate = getLatestFileNameFor(NseCo.DM_FILES_PATH, ApCo.PRA_DM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        mtDate = ProCo.extractDate(mtDate);

        if (cmDate == null || foDate == null || mtDate == null) {
            return null;
        } else if (cmDate.equals(foDate) && foDate.equals(mtDate)) {
            return cmDate;
        } else {
            LOGGER.warn("Not All files are available: fo=[fo-{}], cm=[cm-{}], mt=[mt-{}]", foDate, cmDate, mtDate);
            LOGGER.info("fo=[fo-{}]", foDate);
            LOGGER.info("cm=[cm-{}]", cmDate);
            LOGGER.info("mt=[mt-{}]", mtDate);
            //throw new RuntimeException("All Files Does Not Exist: ABORTING");
            return null;
        }
    }

}
