package org.pra.nse.csv.download;

import org.pra.nse.ApCo;
import org.pra.nse.NseCo;
import org.pra.nse.util.DateUtils;
import org.pra.nse.util.NseFileUtils;
import org.pra.nse.util.PraFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FmDownloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FmDownloader.class);

    private final String Base_Url = NseCo.FM_BASE_URL;
    private final String Data_Dir = ApCo.ROOT_DIR + File.separator + NseCo.FM_DIR_NAME;
    private final String File_Prefix = NseCo.NSE_FM_FILE_PREFIX;
    private final String File_Suffix = NseCo.NSE_FM_FILE_SUFFIX;
    private final String File_Ext = NseCo.NSE_FM_FILE_EXT;
    private final String File_Date_Regex = NseCo.NSE_FM_FILE_NAME_DATE_REGEX;
    private final String File_Date_Format = NseCo.NSE_FM_FILE_NAME_DATE_FORMAT;
    private final DateTimeFormatter File_Date_Dtf = NseCo.FM_FILE_NAME_DTF;
    private final String Data_Date_Regex = null;
    private final String Data_Date_Format = null;
    private final DateTimeFormatter Data_Date_Dtf = null;

    private final NseFileUtils nseFileUtils;
    private final PraFileUtils praFileUtils;

    private final DownloadHelper downloadHelper;

    public FmDownloader(NseFileUtils nseFileUtils, PraFileUtils praFileUtils, DownloadHelper downloadHelper) {
        this.nseFileUtils = nseFileUtils;
        this.praFileUtils = praFileUtils;
        this.downloadHelper = downloadHelper;
    }

    public void downloadFromDefaultDate() {
        downloadFromDate(ApCo.DOWNLOAD_FROM_DATE);
    }
    public void downloadFromDate(LocalDate fromDate) {
        List<String> filesDownloadUrls = prepareFileUrls(fromDate);
        looper(filesDownloadUrls);
    }

    public void downloadFromLastDate() {
        String str = praFileUtils.getLatestFileNameFor(Data_Dir, ApCo.PRA_FM_FILE_PREFIX, ApCo.REPORTS_FILE_EXT, 1);
        LocalDate dateOfLatestFile = DateUtils.getLocalDateFromPath(str);
        List<String> filesDownloadUrls = prepareFileUrls(dateOfLatestFile.plusDays(1));
        if(filesDownloadUrls == null || filesDownloadUrls.size() == 0) return;

        LocalDate dateOfNextFile = DateUtils.getLocalDateFromPath(filesDownloadUrls.get(0), File_Date_Regex, File_Date_Format);
        if(filesDownloadUrls.size() == 1 && dateOfNextFile.isBefore(LocalDate.now())) {
            download(filesDownloadUrls.get(0));
        } else {
            looper(filesDownloadUrls);
        }
    }


    private List<String> prepareFileUrls(LocalDate fromDate) {
        List<String> filesToBeDownloaded = nseFileUtils.constructFileNames(fromDate, File_Date_Format, File_Prefix,File_Suffix + File_Ext);
        filesToBeDownloaded.removeAll(nseFileUtils.fetchFileNames(Data_Dir, null, null));
        return nseFileUtils.constructFileDownloadUrlWithYearAndMonth(Base_Url, filesToBeDownloaded);
    }

//    private void looper(List<String> urlListToBeDownloaded) {
//        if(downloadHelper.shouldDownload(urlListToBeDownloaded)) {
//            urlListToBeDownloaded.stream().forEach( fileUrl -> {
//                download(fileUrl);
//            });
//        }
//    }
    private void looper(List<String> urlListToBeDownloaded) {
        urlListToBeDownloaded.stream().filter( fileUrl -> {
            LocalDate forDate = DateUtils.getLocalDateFromPath(fileUrl, File_Date_Regex, File_Date_Format);
            LOGGER.info("filter - forDate: {} ({})", forDate, fileUrl);
            return downloadHelper.timeFilter(forDate);
        }).forEach( filteredFileUrl -> {
            LOGGER.info("download - forDate: {}", filteredFileUrl);
            download(filteredFileUrl);
        });
    }

    private void download(String fileUrl) {
        //TODO 28-Aug-2019 file not to be downloaded as it is corrupt
        downloadHelper.downloadFile(fileUrl, Data_Dir,
                () -> (Data_Dir + File.separator + fileUrl.substring(66, 89)),
                zipFilePathAndName -> {
                    LOGGER.info("PASSING: unzipping file: {}", zipFilePathAndName);
                });
    }

}
