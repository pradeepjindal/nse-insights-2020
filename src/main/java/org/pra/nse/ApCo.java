package org.pra.nse;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApCo {
    public static final LocalDate DOWNLOAD_FROM_DATE = LocalDate.of(2020,1,1);
    public static final LocalTime DAILY_DOWNLOAD_TIME = LocalTime.of(18,0,0,0);

    public static final LocalDate EMAIL_FROM_DATE = LocalDate.of(2020,1,1);
    public static final boolean EMAIL_ENABLED = true;
    public static final boolean EMAIL_ENABLED_FOR_MANISH = true;
    public static final boolean EMAIL_ENABLED_FOR_PRADEEP = true;
    public static final boolean EMAIL_ENABLED_FOR_SHUVI = false;

    public static final String ROOT_DIR = System.getProperty("user.home");

    //https://www.nseindia.com/content/historical/EQUITIES/2019/SEP/cm10SEP2019bhav.csv.zip
//    public static final String CM_BASE_URL = "https://www.nseindia.com/content/historical/EQUITIES";
//    public static final String FO_BASE_URL = "https://www.nseindia.com/content/historical/DERIVATIVES";

    //https://www1.nseindia.com/content/historical/EQUITIES/2019/SEP/cm10SEP2019bhav.csv.zip
    public static final String CM_BASE_URL = "https://www1.nseindia.com/content/historical/EQUITIES";
    public static final String FM_BASE_URL = "https://www1.nseindia.com/content/historical/DERIVATIVES";

    //https://archives.nseindia.com/content/historical/EQUITIES/2020/JAN/cm06JAN2020bhav.csv.zip
//    public static final String CM_BASE_URL = "https://archives.nseindia.com/content/historical/EQUITIES";
//    public static final String FO_BASE_URL = "https://archives.nseindia.com/content/historical/EQUITIES";


    //https://www.nseindia.com/archives/equities/mto/MTO_06012020.DAT
    //public static final String DM_BASE_URL = "https://www.nseindia.com/archives/equities/mto";
    //https://www1.nseindia.com/archives/equities/mto/MTO_06012020.DAT
    public static final String DM_BASE_URL = "https://www1.nseindia.com/archives/equities/mto";

    //https://www.nseindia.com/content/nsccl/fao_participant_vol_13092019.csv
    //public static final String BP_BASE_URL = "https://www.nseindia.com/content/nsccl";
    //https://www1.nseindia.com/content/nsccl/fao_participant_vol_13092019.csv
    public static final String BP_BASE_URL = "https://www1.nseindia.com/content/nsccl";

    //https://archives.nseindia.com/products/content/sec_bhavdata_full_06012020.csv
    public static final String DBC_BASE_URL = "https://archives.nseindia.com/products/content";


    public static final String CM_DIR_NAME = "pra-nse-cm";
    public static final String FM_DIR_NAME = "pra-nse-fm";
    public static final String DM_DIR_NAME = "pra-nse-dm";
    public static final String BP_DIR_NAME = "pra-nse-bp";
    public static final String AB_DIR_NAME = "pra-nse-ab";
    public static final String DBC_DIR_NAME = "pra-nse-bhav-daily";

    public static final String COMPUTE_DIR_NAME = "pra-computed-data";
    public static final String REPORTS_DIR_NAME = "pra-reports-data";
    public static final String REPORTS_DIR_NAME_MANISH = "pra-reports-manish";
    public static final String REPORTS_DIR_NAME_SHUVI = "pra-reports-shuvi";
    public static final String REPORTS_DIR_NAME_TMP = "pra-reports-tmp";

    public static final String CM_FILE_NAME_REGEX = "";
    public static final String FM_FILE_NAME_REGEX = "";
    public static final String DM_FILE_NAME_REGEX = "";
    public static final String AB_FILE_NAME_REGEX = "";
    public static final String DBC_FILE_NAME_REGEX = "";

    public static final String ddMMMyyyy_UPPER_CASE_DATE_REGEX = "\\d{2}[A-Z]{3}\\d{4}";
    public static final String ddMMMyyyy_CAPITAL_CASE_DATE_REGEX = "\\d{2}[A-Z]{1}[a-z]{2}\\d{4}";
    public static final String ddMMyyyy_DATE_REGEX = "\\d{2}\\d{2}\\d{4}";
    public static final String NSE_CM_FILE_NAME_DATE_REGEX = ddMMMyyyy_UPPER_CASE_DATE_REGEX;
    public static final String NSE_FM_FILE_NAME_DATE_REGEX = ddMMMyyyy_UPPER_CASE_DATE_REGEX;
    public static final String NSE_DM_FILE_NAME_DATE_REGEX = ddMMyyyy_DATE_REGEX;
    public static final String NSE_BP_FILE_NAME_DATE_REGEX = ddMMyyyy_DATE_REGEX;
    public static final String NSE_DBC_FILE_NAME_DATE_REGEX = ddMMyyyy_DATE_REGEX;
    public static final String AB_FILE_NAME_DATE_REGEX = ddMMMyyyy_CAPITAL_CASE_DATE_REGEX;

    public static final String DATE_REGEX_yyyyMMdd = "\\d{4}-\\d{2}-\\d{2}";
    public static final String DATA_FILE_NAME_DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String PRA_FILE_NAME_DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String DEFAULT_FILE_NAME_DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

    public static final String ddMMMyyyy_DATE_FORMAT = "ddMMMyyyy";
    public static final String ddMMyyyy_DATE_FORMAT = "ddMMyyyy";
    public static final String NSE_CM_FILE_NAME_DATE_FORMAT = ddMMMyyyy_DATE_FORMAT;
    public static final String NSE_FM_FILE_NAME_DATE_FORMAT = ddMMMyyyy_DATE_FORMAT;
    public static final String NSE_DM_FILE_NAME_DATE_FORMAT = ddMMyyyy_DATE_FORMAT;
    public static final String NSE_BP_FILE_NAME_DATE_FORMAT = ddMMyyyy_DATE_FORMAT;
    public static final String NSE_DBC_FILE_NAME_DATE_FORMAT = ddMMyyyy_DATE_FORMAT;
    public static final String AB_FILE_NAME_DATE_FORMAT = ddMMMyyyy_DATE_FORMAT;

    public static final String DATA_FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PRA_FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";

    public static final String PRA_CM_DATA_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String PRA_FM_DATA_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String PRA_DM_DATA_DATE_FORMAT = "yyyy-MM-dd";
    public static final String AB_DATA_DATE_FORMAT = "yyyyMMdd";
    public static final String REPORTS_DATE_FORMAT = "dd-MMM-yyyy";

    public static final String NSE_CM_FILE_PREFIX = "cm";
    public static final String NSE_FM_FILE_PREFIX = "fo";
    public static final String NSE_DM_FILE_PREFIX = "MTO_";
    public static final String NSE_BP_FILE_PREFIX = "fao_participant_vol_";
    public static final String NSE_DBC_FILE_PREFIX = "sec_bhavdata_full_";

    public static final String PRA_CM_FILE_PREFIX = "cm-";
    public static final String PRA_FM_FILE_PREFIX = "fo-";
    public static final String PRA_DM_FILE_PREFIX = "mt-";
    public static final String PRA_BP_FILE_PREFIX = "bp-";
    public static final String AB_FILE_PREFIX = "EQ_";

    public static final String NSE_CM_FILE_SUFFIX = "bhav.csv";
    public static final String NSE_FM_FILE_SUFFIX = "bhav.csv";
    public static final String NSE_DM_FILE_SUFFIX = "";
    public static final String NSE_BP_FILE_SUFFIX = "";
    public static final String AB_FILE_SUFFIX = "";
    public static final String NSE_DBC_FILE_SUFFIX = "";

    public static final String NSE_CM_FILE_EXT = ".zip";
    public static final String NSE_FM_FILE_EXT = ".zip";
    public static final String NSE_DM_FILE_EXT = ".DAT";
    public static final String NSE_BP_FILE_EXT = ".csv";
    public static final String NSE_DBC_FILE_EXT = ".csv";
    public static final String AB_FILE_EXT = ".txt";

    public static final String DATA_FILE_EXT = ".csv";
    public static final String REPORTS_FILE_EXT = ".csv";
    public static final String DEFAULT_FILE_EXT = ".csv";

    public static final String PRADEEP_FILE_NAME = "pradeepData";
    public static final String SHUVI_FILE_NAME = "shuviData";

    public static final String MANISH_FILE_NAME = "manishData";
    public static final String MANISH_FILE_NAME_B = "manishDataB";

    //public static final String REPORTS_PATH = ROOT_DIR + File.separator + REPORTS_DIR_NAME


    public static final DateTimeFormatter ddMMMyyyy_DTF = DateTimeFormatter.ofPattern(ApCo.ddMMMyyyy_DATE_FORMAT);
    public static final DateTimeFormatter ddMMyyyy_DTF = DateTimeFormatter.ofPattern(ApCo.ddMMyyyy_DATE_FORMAT);
    public static final DateTimeFormatter DATA_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.DATA_FILE_NAME_DATE_FORMAT);

    public static final DateTimeFormatter CM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.NSE_CM_FILE_NAME_DATE_FORMAT);
    public static final String CM_FILES_PATH = ApCo.ROOT_DIR + File.separator + ApCo.CM_DIR_NAME;

    public static final DateTimeFormatter FM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.NSE_FM_FILE_NAME_DATE_FORMAT);
    public static final String FM_FILES_PATH = ApCo.ROOT_DIR + File.separator + ApCo.FM_DIR_NAME;

    public static final DateTimeFormatter DM_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.NSE_DM_FILE_NAME_DATE_FORMAT);
    public static final String DM_FILES_PATH = ApCo.ROOT_DIR + File.separator + ApCo.DM_DIR_NAME;

    public static final DateTimeFormatter DBC_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.NSE_DBC_FILE_NAME_DATE_FORMAT);

    public static final DateTimeFormatter AB_FILE_NAME_DTF = DateTimeFormatter.ofPattern(ApCo.AB_FILE_NAME_DATE_FORMAT);
    public static final DateTimeFormatter AB_DATA_DTF = DateTimeFormatter.ofPattern(ApCo.AB_DATA_DATE_FORMAT);

    public static final DateTimeFormatter PRA_DTF = DateTimeFormatter.ofPattern(ApCo.PRA_FILE_NAME_DATE_FORMAT);
    public static final String PRA_FILES_PATH = ApCo.ROOT_DIR + File.separator + ApCo.REPORTS_DIR_NAME;

    public static final DateTimeFormatter DEFAULT_DTF = DateTimeFormatter.ofPattern(ApCo.DEFAULT_FILE_NAME_DATE_FORMAT);
    public static final String DEFAULT_FILES_PATH = ApCo.ROOT_DIR + File.separator + ApCo.ROOT_DIR;
}
