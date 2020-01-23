package org.pra.nse.calculation;

public class CalcCons {
    public static final String RSI_DATA_FILE_PREFIX = "rsi";
    public static final String MFI_DATA_FILE_PREFIX = "mfi";
    public static final String AVG_DATA_FILE_PREFIX = "avg";

    public static final String RSI_CSV_HEADER
            = "symbol,trade_date,OpenRsi10Ema,HighRsi10Ema,LowRsi10Ema,CloseRsi10Ema,LastRsi10Ema,AtpRsi10Ema,HlmRsi10Ema";
    public static final String MFI_CSV_HEADER = "symbol,trade_date," +
            "volumeAtpMfi05,volumeAtpMfi10,volumeAtpMfi15,volumeAtpMfi20," +
            "deliveryAtpMfi05,deliveryAtpMfi10,deliveryAtpMfi15,deliveryAtpMfi20";
    public static final String AVG_CSV_HEADER = "symbol,trade_date," +
            "atpAvg05,atpAvg10,atpAvg15,atpAvg20," +
            "volumeAvg05,volumeAvg10,volumeAvg15,volumeAvg20," +
            "deliveryAvg05,deliveryAvg10,deliveryAvg15,deliveryAvg20," +
            "oiAvg05,oiAvg10,oiAvg15,oiAvg20";
}
