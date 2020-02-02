package org.pra.nse.report;

import java.util.List;

public class ReportConstants {
    public static final String DSRD = "DeliverySpikeReportDaily";
    public static final String DSRF = "DeliverySpikeReportFull";

    public static final String PPF_10 = "PPF_10_Report";
    public static final String PPF_20 = "PPF_20_Report";
    public static final String PPF_FULL = "PPF_days_Full_Report";

    static final String DSRF_CSV_HEADER =
            "symbol,trade_date," +
            "open,high,low,close,last,atp,hlm," +
            "open_ChgPrcnt,high_ChgPrcnt,low_ChgPrcnt,close_ChgPrcnt,last_ChgPrcnt,atp_ChgPrcnt," +
            "traded_ChgPrcnt,delivered_ChgPrcnt,oiChgPrcnt,premium," +
            "othighPrcnt,otlowPrcnt,otclosePrcnt,otlastPrcnt,otatpPrcnt," +
            "tdyAtpRsi10Ema,tdyCloseRsi10Ema,tdyLastRsi10Ema";

    static final String abc = "Tally,Growth,Spike,Bell";

    static final String PPF_CSV_HEADER =
            "symbol,trade_date," +
            "open,high,low,hlp,close,last,closingBell,closeToLastPct,atp,hlm," +

            "calcAtpFixGrowth,calcAtpDynGrowth,atpChgPct," +
            "calcVolFixGrowth,calcVolDynGrowth,volChgPct," +
            "delTally,calcDelFixGrowth,calcDelDynGrowth,delChgPct," +
            "calcFoiFixGrowth,calcFoiDynGrowth,foiChgPct," +

            "premium,openingBell," +
            "nxtCloseToOpenPct,nxtOptoHighPct,nxtOptoLowPct,nxtOptoAtpPct," +
            "VolAtpMfi10,DelAtpMfi10," +
            "AtpRsi10Ema,CloseRsi10Ema,LastRsi10Ema";

    static final List<String> SHUVI_TICKERS =
            List.of("ASIANPAINTS", "COLPAL", "DABUR", "ICICIPRULI", "JUBLIENTFOOD", "PIDILITE", "RELIANCE", "SBIN", "TITAN", "UBL");
}
