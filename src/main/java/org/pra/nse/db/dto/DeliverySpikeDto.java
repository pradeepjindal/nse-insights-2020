package org.pra.nse.db.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeliverySpikeDto {
    private String symbol;
    private LocalDate tradeDate;

    private BigDecimal previousClose;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal highLowPct;
    private BigDecimal close;
    private BigDecimal last;
    private BigDecimal atp;
    private BigDecimal highLowMid;
    private BigDecimal highLowPrcnt;
    private String closingBell;
    private BigDecimal closeToLastPercent;

    private BigDecimal openChgPrcnt;
    private BigDecimal highChgPrcnt;
    private BigDecimal lowChgPrcnt;
    private BigDecimal closeChgPrcnt;
    private BigDecimal lastChgPrcnt;
    private BigDecimal atpChgPrcnt;

    private BigDecimal atpFixGrowth;
    private BigDecimal atpDynGrowth;
    private BigDecimal volFixGrowth;
    private BigDecimal volDynGrowth;
    private BigDecimal tradedChgPrcnt;
    private BigDecimal delFixGrowth;
    private BigDecimal delDynGrowth;
    private BigDecimal deliveredChgPrcnt;
    private BigDecimal foiFixGrowth;
    private BigDecimal foiDynGrowth;
    private BigDecimal oiChgPrcnt;
    private BigDecimal premium;

    private String openingBell;
    private BigDecimal closeToOpenPercent;

    private BigDecimal othighPrcnt;
    private BigDecimal otlowPrcnt;
    private BigDecimal otclosePrcnt;
    private BigDecimal otlastPrcnt;
    private BigDecimal otatpPrcnt;

    private BigDecimal tdycloseMinusYesclose;
    private BigDecimal tdylastMinusYeslast;
    private BigDecimal tdyatpMinusYesatp;

    private BigDecimal volume;
    private BigDecimal delivery;
    private BigDecimal oi;

    private BigDecimal volAtpMfi10;
    private BigDecimal delAtpMfi10;

    private BigDecimal tdyCloseRsi10Sma;
    private BigDecimal tdyLastRsi10Sma;
    private BigDecimal tdyAtpRsi10Sma;

    private BigDecimal nxtCloseToOpenPercent;
    private BigDecimal nxtOptoHighPrcnt;
    private BigDecimal nxtOptoLowPrcnt;
    private BigDecimal nxtOptoAtpPrcnt;

    private BigDecimal atpAvg10;
    private BigDecimal volAvg10;
    private BigDecimal delAvg10;
    private BigDecimal foiAvg10;

    private BigDecimal atpAvg20;
    private BigDecimal volAvg20;
    private BigDecimal delAvg20;
    private BigDecimal foiAvg20;

    private BigDecimal delAccumulation;

    public String toCsvString() {
        return  symbol + ","
                + tradeDate + ","

                + open + ","
                + high + ","
                + low + ","
                + close + ","
                + tradedChgPrcnt + ","
                + deliveredChgPrcnt + ","
                + othighPrcnt + ","
                + otlowPrcnt;
    }

    public String toFullCsvString() {
        return  symbol + ","
                + tradeDate + ","

                + open + ","
                + high + ","
                + low + ","
                + close + ","
                + last + ","
                + atp + ","
                + highLowMid + ","

                + openChgPrcnt + ","
                + highChgPrcnt + ","
                + lowChgPrcnt + ","
                + closeChgPrcnt + ","
                + lastChgPrcnt + ","
                + atpChgPrcnt + ","

                + tradedChgPrcnt + ","
                + deliveredChgPrcnt + ","
                + oiChgPrcnt + ","
                + premium + ","

                + othighPrcnt + ","
                + otlowPrcnt + ","
                + otclosePrcnt + ","
                + otlastPrcnt + ","
                + otatpPrcnt + ","

//                + tdycloseMinusYesclose + ","
//                + tdylastMinusYeslast + ","
//                + tdyatpMinusYesatp + ","

                + tdyCloseRsi10Sma + ","
                + tdyLastRsi10Sma + ","
                + tdyAtpRsi10Sma;
    }

    public String toPpfString() {
        return  symbol + ","
                + tradeDate + ","

                + open + ","
                + high + ","
                + low + ","
                + close + ","
                + last + ","
                + closingBell + ","
                + closeToLastPercent + ","
                + atp + ","
                + highLowMid + ","

                + tradedChgPrcnt + ","
                + deliveredChgPrcnt + ","
                + oiChgPrcnt + ","
                + premium + ","
                + openingBell + ","
                + closeToOpenPercent + ","
                + othighPrcnt + ","
                + otlowPrcnt + ","
                + otatpPrcnt + ","

                + volAtpMfi10 + ","
                + delAtpMfi10 + ","
                + tdyCloseRsi10Sma + ","
                + tdyLastRsi10Sma + ","
                + tdyAtpRsi10Sma;
    }

    public String toPpfString2() {
        return  symbol + ","
                + tradeDate + ","

                + open + ","
                + high + ","
                + low + ","
                + highLowPct + ","
                + close + ","
                + last + ","
                + closingBell + ","
                + closeToLastPercent + ","
                + atp + ","
                + highLowMid + ","

                + atpFixGrowth + ","
                + atpDynGrowth + ","
                + atpChgPrcnt + ","
                + volFixGrowth + ","
                + volDynGrowth + ","
                + tradedChgPrcnt + ","

                + delAccumulation + ","
                + delFixGrowth + ","
                + delDynGrowth + ","
                + deliveredChgPrcnt + ","
                + foiFixGrowth + ","
                + foiDynGrowth + ","
                + oiChgPrcnt + ","

                + premium + ","
                + openingBell + ","

                + nxtCloseToOpenPercent + ","
                + nxtOptoHighPrcnt + ","
                + nxtOptoLowPrcnt + ","
                + nxtOptoAtpPrcnt + ","

                + volAtpMfi10 + ","
                + delAtpMfi10 + ","
                + tdyCloseRsi10Sma + ","
                + tdyLastRsi10Sma + ","
                + tdyAtpRsi10Sma;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHighLowPct() {
        return highLowPct;
    }

    public void setHighLowPct(BigDecimal highLowPct) {
        this.highLowPct = highLowPct;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getAtp() {
        return atp;
    }

    public void setAtp(BigDecimal atp) {
        this.atp = atp;
    }

    public BigDecimal getHighLowMid() {
        return highLowMid;
    }

    public void setHighLowMid(BigDecimal highLowMid) {
        this.highLowMid = highLowMid;
    }

    public BigDecimal getHighLowPrcnt() {
        return highLowPrcnt;
    }

    public void setHighLowPrcnt(BigDecimal highLowPrcnt) {
        this.highLowPrcnt = highLowPrcnt;
    }

    public String getClosingBell() {
        return closingBell;
    }

    public void setClosingBell(String closingBell) {
        this.closingBell = closingBell;
    }

    public BigDecimal getCloseToLastPercent() {
        return closeToLastPercent;
    }

    public void setCloseToLastPercent(BigDecimal closeToLastPercent) {
        this.closeToLastPercent = closeToLastPercent;
    }

    public BigDecimal getOpenChgPrcnt() {
        return openChgPrcnt;
    }

    public void setOpenChgPrcnt(BigDecimal openChgPrcnt) {
        this.openChgPrcnt = openChgPrcnt;
    }

    public BigDecimal getHighChgPrcnt() {
        return highChgPrcnt;
    }

    public void setHighChgPrcnt(BigDecimal highChgPrcnt) {
        this.highChgPrcnt = highChgPrcnt;
    }

    public BigDecimal getLowChgPrcnt() {
        return lowChgPrcnt;
    }

    public void setLowChgPrcnt(BigDecimal lowChgPrcnt) {
        this.lowChgPrcnt = lowChgPrcnt;
    }

    public BigDecimal getCloseChgPrcnt() {
        return closeChgPrcnt;
    }

    public void setCloseChgPrcnt(BigDecimal closeChgPrcnt) {
        this.closeChgPrcnt = closeChgPrcnt;
    }

    public BigDecimal getLastChgPrcnt() {
        return lastChgPrcnt;
    }

    public void setLastChgPrcnt(BigDecimal lastChgPrcnt) {
        this.lastChgPrcnt = lastChgPrcnt;
    }

    public BigDecimal getAtpChgPrcnt() {
        return atpChgPrcnt;
    }

    public void setAtpChgPrcnt(BigDecimal atpChgPrcnt) {
        this.atpChgPrcnt = atpChgPrcnt;
    }

    public BigDecimal getAtpFixGrowth() {
        return atpFixGrowth;
    }

    public void setAtpFixGrowth(BigDecimal atpFixGrowth) {
        this.atpFixGrowth = atpFixGrowth;
    }

    public BigDecimal getVolFixGrowth() {
        return volFixGrowth;
    }

    public void setVolFixGrowth(BigDecimal volFixGrowth) {
        this.volFixGrowth = volFixGrowth;
    }

    public BigDecimal getTradedChgPrcnt() {
        return tradedChgPrcnt;
    }

    public void setTradedChgPrcnt(BigDecimal tradedChgPrcnt) {
        this.tradedChgPrcnt = tradedChgPrcnt;
    }

    public BigDecimal getDelFixGrowth() {
        return delFixGrowth;
    }

    public void setDelFixGrowth(BigDecimal delFixGrowth) {
        this.delFixGrowth = delFixGrowth;
    }

    public BigDecimal getDeliveredChgPrcnt() {
        return deliveredChgPrcnt;
    }

    public void setDeliveredChgPrcnt(BigDecimal deliveredChgPrcnt) {
        this.deliveredChgPrcnt = deliveredChgPrcnt;
    }

    public BigDecimal getFoiFixGrowth() {
        return foiFixGrowth;
    }

    public void setFoiFixGrowth(BigDecimal foiFixGrowth) {
        this.foiFixGrowth = foiFixGrowth;
    }

    public BigDecimal getOiChgPrcnt() {
        return oiChgPrcnt;
    }

    public void setOiChgPrcnt(BigDecimal oiChgPrcnt) {
        this.oiChgPrcnt = oiChgPrcnt;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public String getOpeningBell() {
        return openingBell;
    }

    public void setOpeningBell(String openingBell) {
        this.openingBell = openingBell;
    }

    public BigDecimal getCloseToOpenPercent() {
        return closeToOpenPercent;
    }

    public void setCloseToOpenPercent(BigDecimal closeToOpenPercent) {
        this.closeToOpenPercent = closeToOpenPercent;
    }

    public BigDecimal getOthighPrcnt() {
        return othighPrcnt;
    }

    public void setOthighPrcnt(BigDecimal othighPrcnt) {
        this.othighPrcnt = othighPrcnt;
    }

    public BigDecimal getOtlowPrcnt() {
        return otlowPrcnt;
    }

    public void setOtlowPrcnt(BigDecimal otlowPrcnt) {
        this.otlowPrcnt = otlowPrcnt;
    }

    public BigDecimal getOtclosePrcnt() {
        return otclosePrcnt;
    }

    public void setOtclosePrcnt(BigDecimal otclosePrcnt) {
        this.otclosePrcnt = otclosePrcnt;
    }

    public BigDecimal getOtlastPrcnt() {
        return otlastPrcnt;
    }

    public void setOtlastPrcnt(BigDecimal otlastPrcnt) {
        this.otlastPrcnt = otlastPrcnt;
    }

    public BigDecimal getOtatpPrcnt() {
        return otatpPrcnt;
    }

    public void setOtatpPrcnt(BigDecimal otatpPrcnt) {
        this.otatpPrcnt = otatpPrcnt;
    }

    public BigDecimal getTdycloseMinusYesclose() {
        return tdycloseMinusYesclose;
    }

    public void setTdycloseMinusYesclose(BigDecimal tdycloseMinusYesclose) {
        this.tdycloseMinusYesclose = tdycloseMinusYesclose;
    }

    public BigDecimal getTdylastMinusYeslast() {
        return tdylastMinusYeslast;
    }

    public void setTdylastMinusYeslast(BigDecimal tdylastMinusYeslast) {
        this.tdylastMinusYeslast = tdylastMinusYeslast;
    }

    public BigDecimal getTdyatpMinusYesatp() {
        return tdyatpMinusYesatp;
    }

    public void setTdyatpMinusYesatp(BigDecimal tdyatpMinusYesatp) {
        this.tdyatpMinusYesatp = tdyatpMinusYesatp;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getDelivery() {
        return delivery;
    }

    public void setDelivery(BigDecimal delivery) {
        this.delivery = delivery;
    }

    public BigDecimal getOi() {
        return oi;
    }

    public void setOi(BigDecimal oi) {
        this.oi = oi;
    }

    public BigDecimal getVolAtpMfi10() {
        return volAtpMfi10;
    }

    public void setVolAtpMfi10(BigDecimal volAtpMfi10) {
        this.volAtpMfi10 = volAtpMfi10;
    }

    public BigDecimal getDelAtpMfi10() {
        return delAtpMfi10;
    }

    public void setDelAtpMfi10(BigDecimal delAtpMfi10) {
        this.delAtpMfi10 = delAtpMfi10;
    }

    public BigDecimal getTdyCloseRsi10Sma() {
        return tdyCloseRsi10Sma;
    }

    public void setTdyCloseRsi10Sma(BigDecimal tdyCloseRsi10Sma) {
        this.tdyCloseRsi10Sma = tdyCloseRsi10Sma;
    }

    public BigDecimal getTdyLastRsi10Sma() {
        return tdyLastRsi10Sma;
    }

    public void setTdyLastRsi10Sma(BigDecimal tdyLastRsi10Sma) {
        this.tdyLastRsi10Sma = tdyLastRsi10Sma;
    }

    public BigDecimal getTdyAtpRsi10Sma() {
        return tdyAtpRsi10Sma;
    }

    public void setTdyAtpRsi10Sma(BigDecimal tdyAtpRsi10Sma) {
        this.tdyAtpRsi10Sma = tdyAtpRsi10Sma;
    }

    public BigDecimal getNxtCloseToOpenPercent() {
        return nxtCloseToOpenPercent;
    }

    public void setNxtCloseToOpenPercent(BigDecimal nxtCloseToOpenPercent) {
        this.nxtCloseToOpenPercent = nxtCloseToOpenPercent;
    }

    public BigDecimal getNxtOptoHighPrcnt() {
        return nxtOptoHighPrcnt;
    }

    public void setNxtOptoHighPrcnt(BigDecimal nxtOptoHighPrcnt) {
        this.nxtOptoHighPrcnt = nxtOptoHighPrcnt;
    }

    public BigDecimal getNxtOptoLowPrcnt() {
        return nxtOptoLowPrcnt;
    }

    public void setNxtOptoLowPrcnt(BigDecimal nxtOptoLowPrcnt) {
        this.nxtOptoLowPrcnt = nxtOptoLowPrcnt;
    }

    public BigDecimal getNxtOptoAtpPrcnt() {
        return nxtOptoAtpPrcnt;
    }

    public void setNxtOptoAtpPrcnt(BigDecimal nxtOptoAtpPrcnt) {
        this.nxtOptoAtpPrcnt = nxtOptoAtpPrcnt;
    }

    public BigDecimal getAtpAvg10() {
        return atpAvg10;
    }

    public void setAtpAvg10(BigDecimal atpAvg10) {
        this.atpAvg10 = atpAvg10;
    }

    public BigDecimal getVolAvg10() {
        return volAvg10;
    }

    public void setVolAvg10(BigDecimal volAvg10) {
        this.volAvg10 = volAvg10;
    }

    public BigDecimal getDelAvg10() {
        return delAvg10;
    }

    public void setDelAvg10(BigDecimal delAvg10) {
        this.delAvg10 = delAvg10;
    }

    public BigDecimal getFoiAvg10() {
        return foiAvg10;
    }

    public void setFoiAvg10(BigDecimal foiAvg10) {
        this.foiAvg10 = foiAvg10;
    }

    public BigDecimal getAtpAvg20() {
        return atpAvg20;
    }

    public void setAtpAvg20(BigDecimal atpAvg20) {
        this.atpAvg20 = atpAvg20;
    }

    public BigDecimal getVolAvg20() {
        return volAvg20;
    }

    public void setVolAvg20(BigDecimal volAvg20) {
        this.volAvg20 = volAvg20;
    }

    public BigDecimal getDelAvg20() {
        return delAvg20;
    }

    public void setDelAvg20(BigDecimal delAvg20) {
        this.delAvg20 = delAvg20;
    }

    public BigDecimal getFoiAvg20() {
        return foiAvg20;
    }

    public void setFoiAvg20(BigDecimal foiAvg20) {
        this.foiAvg20 = foiAvg20;
    }

    public BigDecimal getAtpDynGrowth() {
        return atpDynGrowth;
    }

    public void setAtpDynGrowth(BigDecimal atpDynGrowth) {
        this.atpDynGrowth = atpDynGrowth;
    }

    public BigDecimal getVolDynGrowth() {
        return volDynGrowth;
    }

    public void setVolDynGrowth(BigDecimal volDynGrowth) {
        this.volDynGrowth = volDynGrowth;
    }

    public BigDecimal getDelDynGrowth() {
        return delDynGrowth;
    }

    public void setDelDynGrowth(BigDecimal delDynGrowth) {
        this.delDynGrowth = delDynGrowth;
    }

    public BigDecimal getFoiDynGrowth() {
        return foiDynGrowth;
    }

    public void setFoiDynGrowth(BigDecimal foiDynGrowth) {
        this.foiDynGrowth = foiDynGrowth;
    }

    public BigDecimal getDelAccumulation() {
        return delAccumulation;
    }

    public void setDelAccumulation(BigDecimal delAccumulation) {
        this.delAccumulation = delAccumulation;
    }

}
