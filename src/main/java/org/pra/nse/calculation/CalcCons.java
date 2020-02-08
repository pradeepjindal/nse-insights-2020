package org.pra.nse.calculation;

import org.pra.nse.ApCo;

import java.io.File;

public class CalcCons {

    public static final String CALC_FOLDER_NAME = "calc";
    public static final String CALC_DIR_PREFIX = CALC_FOLDER_NAME + "-";

    public static final String AVG_DATA_NAME = "avg";
    public static final String RSI_DATA_NAME = "rsi";
    public static final String MFI_DATA_NAME = "mfi";

    public static final String AVG_DIR_NAME = CALC_DIR_PREFIX + AVG_DATA_NAME;
    public static final String RSI_DIR_NAME = CALC_DIR_PREFIX + RSI_DATA_NAME;
    public static final String MFI_DIR_NAME = CALC_DIR_PREFIX + MFI_DATA_NAME;

    public static final String AVG_FILE_PREFIX = AVG_DATA_NAME + "-";
    public static final String RSI_FILE_PREFIX = RSI_DATA_NAME + "-";
    public static final String MFI_FILE_PREFIX = MFI_DATA_NAME + "-";

    public static final String AVG_FILES_PATH = ApCo.ROOT_DIR +File.separator+ AVG_DIR_NAME;
    public static final String RSI_FILES_PATH = ApCo.ROOT_DIR +File.separator+ RSI_DIR_NAME;
    public static final String MFI_FILES_PATH = ApCo.ROOT_DIR +File.separator+ MFI_DIR_NAME;


    public static final String RSI_CSV_HEADER
            = "symbol,trade_date," +
            "OpenRsi10,HighRsi10,LowRsi10,CloseRsi10,LastRsi10,AtpRsi10,HlmRsi10," +
            "OpenRsi20,HighRsi20,LowRsi20,CloseRsi20,LastRsi20,AtpRsi20,HlmRsi20";
    public static final String MFI_CSV_HEADER = "symbol,trade_date," +
            "volAtpMfi05,volAtpMfi10,volAtpMfi15,volAtpMfi20," +
            "delAtpMfi05,delAtpMfi10,delAtpMfi15,delAtpMfi20";
    public static final String AVG_CSV_HEADER = "symbol,trade_date," +
            "atpAvg05,atpAvg10,atpAvg15,atpAvg20," +
            "volAvg05,volAvg10,volAvg15,volAvg20," +
            "delAvg05,delAvg10,delAvg15,delAvg20," +
            "oiAvg05,oiAvg10,oiAvg15,oiAvg20";

}
