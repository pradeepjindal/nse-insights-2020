package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RsiBean implements CalcBean {

    private String symbol;
    //@JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal openRsi05;
    private BigDecimal highRsi05;
    private BigDecimal lowRsi05;
    private BigDecimal closeRsi05;
    private BigDecimal lastRsi05;
    private BigDecimal atpRsi05;
    private BigDecimal hlmRsi05;
    private BigDecimal ohlcRsi05;

    private BigDecimal openRsi10;
    private BigDecimal highRsi10;
    private BigDecimal lowRsi10;
    private BigDecimal closeRsi10;
    private BigDecimal lastRsi10;
    private BigDecimal atpRsi10;
    private BigDecimal hlmRsi10;
    private BigDecimal ohlcRsi10;

    private BigDecimal openRsi20;
    private BigDecimal highRsi20;
    private BigDecimal lowRsi20;
    private BigDecimal closeRsi20;
    private BigDecimal lastRsi20;
    private BigDecimal atpRsi20;
    private BigDecimal hlmRsi20;
    private BigDecimal ohlcRsi20;

    public String toCsvString() {
        return symbol +
                "," + tradeDate +

                "," + openRsi05 +
                "," + highRsi05 +
                "," + lowRsi05 +
                "," + closeRsi05 +
                "," + lastRsi05 +
                "," + atpRsi05 +
                "," + hlmRsi05 +
                "," + ohlcRsi05 +

                "," + openRsi10 +
                "," + highRsi10 +
                "," + lowRsi10 +
                "," + closeRsi10 +
                "," + lastRsi10 +
                "," + atpRsi10 +
                "," + hlmRsi10 +
                "," + ohlcRsi10 +

                "," + openRsi20 +
                "," + highRsi20 +
                "," + lowRsi20 +
                "," + closeRsi20 +
                "," + lastRsi20 +
                "," + atpRsi20 +
                "," + hlmRsi20 +
                "," + ohlcRsi20;
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

    public BigDecimal getOpenRsi05() {
        return openRsi05;
    }

    public void setOpenRsi05(BigDecimal openRsi05) {
        this.openRsi05 = openRsi05;
    }

    public BigDecimal getHighRsi05() {
        return highRsi05;
    }

    public void setHighRsi05(BigDecimal highRsi05) {
        this.highRsi05 = highRsi05;
    }

    public BigDecimal getLowRsi05() {
        return lowRsi05;
    }

    public void setLowRsi05(BigDecimal lowRsi05) {
        this.lowRsi05 = lowRsi05;
    }

    public BigDecimal getCloseRsi05() {
        return closeRsi05;
    }

    public void setCloseRsi05(BigDecimal closeRsi05) {
        this.closeRsi05 = closeRsi05;
    }

    public BigDecimal getLastRsi05() {
        return lastRsi05;
    }

    public void setLastRsi05(BigDecimal lastRsi05) {
        this.lastRsi05 = lastRsi05;
    }

    public BigDecimal getAtpRsi05() {
        return atpRsi05;
    }

    public void setAtpRsi05(BigDecimal atpRsi05) {
        this.atpRsi05 = atpRsi05;
    }

    public BigDecimal getHlmRsi05() {
        return hlmRsi05;
    }

    public void setHlmRsi05(BigDecimal hlmRsi05) {
        this.hlmRsi05 = hlmRsi05;
    }

    public BigDecimal getOhlcRsi05() {
        return ohlcRsi05;
    }

    public void setOhlcRsi05(BigDecimal ohlcRsi05) {
        this.ohlcRsi05 = ohlcRsi05;
    }

    public BigDecimal getOpenRsi10() {
        return openRsi10;
    }

    public void setOpenRsi10(BigDecimal openRsi10) {
        this.openRsi10 = openRsi10;
    }

    public BigDecimal getHighRsi10() {
        return highRsi10;
    }

    public void setHighRsi10(BigDecimal highRsi10) {
        this.highRsi10 = highRsi10;
    }

    public BigDecimal getLowRsi10() {
        return lowRsi10;
    }

    public void setLowRsi10(BigDecimal lowRsi10) {
        this.lowRsi10 = lowRsi10;
    }

    public BigDecimal getCloseRsi10() {
        return closeRsi10;
    }

    public void setCloseRsi10(BigDecimal closeRsi10) {
        this.closeRsi10 = closeRsi10;
    }

    public BigDecimal getLastRsi10() {
        return lastRsi10;
    }

    public void setLastRsi10(BigDecimal lastRsi10) {
        this.lastRsi10 = lastRsi10;
    }

    public BigDecimal getAtpRsi10() {
        return atpRsi10;
    }

    public void setAtpRsi10(BigDecimal atpRsi10) {
        this.atpRsi10 = atpRsi10;
    }

    public BigDecimal getHlmRsi10() {
        return hlmRsi10;
    }

    public void setHlmRsi10(BigDecimal hlmRsi10) {
        this.hlmRsi10 = hlmRsi10;
    }

    public BigDecimal getOhlcRsi10() {
        return ohlcRsi10;
    }

    public void setOhlcRsi10(BigDecimal ohlcRsi10) {
        this.ohlcRsi10 = ohlcRsi10;
    }

    public BigDecimal getOpenRsi20() {
        return openRsi20;
    }

    public void setOpenRsi20(BigDecimal openRsi20) {
        this.openRsi20 = openRsi20;
    }

    public BigDecimal getHighRsi20() {
        return highRsi20;
    }

    public void setHighRsi20(BigDecimal highRsi20) {
        this.highRsi20 = highRsi20;
    }

    public BigDecimal getLowRsi20() {
        return lowRsi20;
    }

    public void setLowRsi20(BigDecimal lowRsi20) {
        this.lowRsi20 = lowRsi20;
    }

    public BigDecimal getCloseRsi20() {
        return closeRsi20;
    }

    public void setCloseRsi20(BigDecimal closeRsi20) {
        this.closeRsi20 = closeRsi20;
    }

    public BigDecimal getLastRsi20() {
        return lastRsi20;
    }

    public void setLastRsi20(BigDecimal lastRsi20) {
        this.lastRsi20 = lastRsi20;
    }

    public BigDecimal getAtpRsi20() {
        return atpRsi20;
    }

    public void setAtpRsi20(BigDecimal atpRsi20) {
        this.atpRsi20 = atpRsi20;
    }

    public BigDecimal getHlmRsi20() {
        return hlmRsi20;
    }

    public void setHlmRsi20(BigDecimal hlmRsi20) {
        this.hlmRsi20 = hlmRsi20;
    }

    public BigDecimal getOhlcRsi20() {
        return ohlcRsi20;
    }

    public void setOhlcRsi20(BigDecimal ohlcRsi20) {
        this.ohlcRsi20 = ohlcRsi20;
    }
}
