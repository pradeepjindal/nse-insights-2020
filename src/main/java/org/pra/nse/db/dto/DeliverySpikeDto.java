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
    private BigDecimal close;
    private BigDecimal last;
    private BigDecimal atp;
    private BigDecimal highLowMid;

    private String closingBell;
    private BigDecimal closeToLastPercent;

    private BigDecimal openChgPrcnt;
    private BigDecimal highChgPrcnt;
    private BigDecimal lowChgPrcnt;
    private BigDecimal closeChgPrcnt;
    private BigDecimal lastChgPrcnt;
    private BigDecimal atpChgPrcnt;

    private BigDecimal tradedChgPrcnt;
    private BigDecimal deliveredChgPrcnt;
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

    private BigDecimal volumeAtpMfi10;
    private BigDecimal deliveryAtpMfi10;

    private BigDecimal tdyCloseRsi10Ema;
    private BigDecimal tdyLastRsi10Ema;
    private BigDecimal tdyAtpRsi10Ema;




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

                + tdyCloseRsi10Ema + ","
                + tdyLastRsi10Ema + ","
                + tdyAtpRsi10Ema;
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

                + volumeAtpMfi10 + ","
                + deliveryAtpMfi10 + ","
                + tdyCloseRsi10Ema + ","
                + tdyLastRsi10Ema + ","
                + tdyAtpRsi10Ema;
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

    public BigDecimal getTradedChgPrcnt() {
        return tradedChgPrcnt;
    }

    public void setTradedChgPrcnt(BigDecimal tradedChgPrcnt) {
        this.tradedChgPrcnt = tradedChgPrcnt;
    }

    public BigDecimal getDeliveredChgPrcnt() {
        return deliveredChgPrcnt;
    }

    public void setDeliveredChgPrcnt(BigDecimal deliveredChgPrcnt) {
        this.deliveredChgPrcnt = deliveredChgPrcnt;
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

    public BigDecimal getVolumeAtpMfi10() {
        return volumeAtpMfi10;
    }

    public void setVolumeAtpMfi10(BigDecimal volumeAtpMfi10) {
        this.volumeAtpMfi10 = volumeAtpMfi10;
    }

    public BigDecimal getDeliveryAtpMfi10() {
        return deliveryAtpMfi10;
    }

    public void setDeliveryAtpMfi10(BigDecimal deliveryAtpMfi10) {
        this.deliveryAtpMfi10 = deliveryAtpMfi10;
    }

    public BigDecimal getTdyCloseRsi10Ema() {
        return tdyCloseRsi10Ema;
    }

    public void setTdyCloseRsi10Ema(BigDecimal tdyCloseRsi10Ema) {
        this.tdyCloseRsi10Ema = tdyCloseRsi10Ema;
    }

    public BigDecimal getTdyLastRsi10Ema() {
        return tdyLastRsi10Ema;
    }

    public void setTdyLastRsi10Ema(BigDecimal tdyLastRsi10Ema) {
        this.tdyLastRsi10Ema = tdyLastRsi10Ema;
    }

    public BigDecimal getTdyAtpRsi10Ema() {
        return tdyAtpRsi10Ema;
    }

    public void setTdyAtpRsi10Ema(BigDecimal tdyAtpRsi10Ema) {
        this.tdyAtpRsi10Ema = tdyAtpRsi10Ema;
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

    public String getClosingBell() {
        return closingBell;
    }

    public void setClosingBell(String closingBell) {
        this.closingBell = closingBell;
    }

    public String getOpeningBell() {
        return openingBell;
    }

    public void setOpeningBell(String openingBell) {
        this.openingBell = openingBell;
    }

    public BigDecimal getCloseToLastPercent() {
        return closeToLastPercent;
    }

    public void setCloseToLastPercent(BigDecimal closeToLastPercent) {
        this.closeToLastPercent = closeToLastPercent;
    }

    public BigDecimal getCloseToOpenPercent() {
        return closeToOpenPercent;
    }

    public void setCloseToOpenPercent(BigDecimal closeToOpenPercent) {
        this.closeToOpenPercent = closeToOpenPercent;
    }

}
