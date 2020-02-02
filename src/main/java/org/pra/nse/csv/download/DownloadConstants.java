package org.pra.nse.csv.download;

import org.pra.nse.ApCo;
import org.pra.nse.NseCo;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class DownloadConstants {

    public static final DateTimeFormatter CM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(NseCo.NSE_CM_FILE_NAME_DATE_FORMAT);
    public static final String CM_FILES_PATH = ApCo.ROOT_DIR + File.separator + NseCo.CM_DIR_NAME;

    public static final DateTimeFormatter FM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(NseCo.NSE_FM_FILE_NAME_DATE_FORMAT);
    public static final String FM_FILES_PATH = ApCo.ROOT_DIR + File.separator + NseCo.FM_DIR_NAME;

    public static final DateTimeFormatter DM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(NseCo.NSE_DM_FILE_NAME_DATE_FORMAT);
    public static final String DM_FILES_PATH = ApCo.ROOT_DIR + File.separator + NseCo.DM_DIR_NAME;

}
