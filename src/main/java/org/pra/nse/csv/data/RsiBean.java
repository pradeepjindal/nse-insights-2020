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

    private BigDecimal openRsi;
    private BigDecimal highRsi;
    private BigDecimal lowRsi;
    private BigDecimal closeRsi;
    private BigDecimal lastRsi;
    private BigDecimal atpRsi;
    private BigDecimal hlmRsi;
    private BigDecimal ohlcRsi;


    private BigDecimal openRsi03;
    private BigDecimal highRsi03;
    private BigDecimal lowRsi03;
    private BigDecimal closeRsi03;
    private BigDecimal lastRsi03;
    private BigDecimal atpRsi03;
    private BigDecimal hlmRsi03;
    private BigDecimal ohlcRsi03;

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

                "," + openRsi03 +
                "," + highRsi03 +
                "," + lowRsi03 +
                "," + closeRsi03 +
                "," + lastRsi03 +
                "," + atpRsi03 +
                "," + hlmRsi03 +
                "," + ohlcRsi03 +

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

    public BigDecimal getOpenRsi03() {
        return openRsi03;
    }

    public void setOpenRsi03(BigDecimal openRsi03) {
        this.openRsi03 = openRsi03;
    }

    public BigDecimal getHighRsi03() {
        return highRsi03;
    }

    public void setHighRsi03(BigDecimal highRsi03) {
        this.highRsi03 = highRsi03;
    }

    public BigDecimal getLowRsi03() {
        return lowRsi03;
    }

    public void setLowRsi03(BigDecimal lowRsi03) {
        this.lowRsi03 = lowRsi03;
    }

    public BigDecimal getCloseRsi03() {
        return closeRsi03;
    }

    public void setCloseRsi03(BigDecimal closeRsi03) {
        this.closeRsi03 = closeRsi03;
    }

    public BigDecimal getLastRsi03() {
        return lastRsi03;
    }

    public void setLastRsi03(BigDecimal lastRsi03) {
        this.lastRsi03 = lastRsi03;
    }

    public BigDecimal getAtpRsi03() {
        return atpRsi03;
    }

    public void setAtpRsi03(BigDecimal atpRsi03) {
        this.atpRsi03 = atpRsi03;
    }

    public BigDecimal getHlmRsi03() {
        return hlmRsi03;
    }

    public void setHlmRsi03(BigDecimal hlmRsi03) {
        this.hlmRsi03 = hlmRsi03;
    }

    public BigDecimal getOhlcRsi03() {
        return ohlcRsi03;
    }

    public void setOhlcRsi03(BigDecimal ohlcRsi03) {
        this.ohlcRsi03 = ohlcRsi03;
    }



    public BigDecimal getOpenRsi(int days) {
        switch (days) {
            case 3:  openRsi = openRsi03; break;
            case 5:  openRsi = openRsi05; break;
            case 10:  openRsi = openRsi10; break;
            case 20:  openRsi = openRsi20; break;
            default: openRsi = null;
        }
        return openRsi;
    }

    public void setOpenRsi(BigDecimal openRsi) {
        this.openRsi = openRsi;
    }

    public BigDecimal getHighRsi(int days) {
        switch (days) {
            case 3:  highRsi = highRsi03; break;
            case 5:  highRsi = highRsi05; break;
            case 10:  highRsi = highRsi10; break;
            case 20:  highRsi = highRsi20; break;
            default: highRsi = null;
        }
        return highRsi;
    }

    public void setHighRsi(BigDecimal highRsi) {
        this.highRsi = highRsi;
    }

    public BigDecimal getLowRsi(int days) {
        switch (days) {
            case 3:  lowRsi = lowRsi03; break;
            case 5:  lowRsi = lowRsi05; break;
            case 10:  lowRsi = lowRsi10; break;
            case 20:  lowRsi = lowRsi20; break;
            default: lowRsi = null;
        }
        return lowRsi;
    }

    public void setLowRsi(BigDecimal lowRsi) {
        this.lowRsi = lowRsi;
    }

    public BigDecimal getCloseRsi(int days) {
        switch (days) {
            case 3:  closeRsi = closeRsi03; break;
            case 5:  closeRsi = closeRsi05; break;
            case 10:  closeRsi = closeRsi10; break;
            case 20:  closeRsi = closeRsi20; break;
            default: closeRsi = null;
        }
        return closeRsi;
    }

    public void setCloseRsi(BigDecimal closeRsi) {
        this.closeRsi = closeRsi;
    }

    public BigDecimal getLastRsi(int days) {
        switch (days) {
            case 3:  lastRsi = lastRsi03; break;
            case 5:  lastRsi = lastRsi05; break;
            case 10:  lastRsi = lastRsi10; break;
            case 20:  lastRsi = lastRsi20; break;
            default: lastRsi = null;
        }
        return lastRsi;
    }

    public void setLastRsi(BigDecimal lastRsi) {
        this.lastRsi = lastRsi;
    }

    public BigDecimal getAtpRsi(int days) {
        switch (days) {
            case 3:  atpRsi = atpRsi03; break;
            case 5:  atpRsi = atpRsi05; break;
            case 10:  atpRsi = atpRsi10; break;
            case 20:  atpRsi = atpRsi20; break;
            default: atpRsi = null;
        }
        return atpRsi;
    }

    public void setAtpRsi(BigDecimal atpRsi) {
        this.atpRsi = atpRsi;
    }

    public BigDecimal getHlmRsi(int days) {
        switch (days) {
            case 3:  hlmRsi = hlmRsi03; break;
            case 5:  hlmRsi = hlmRsi05; break;
            case 10:  hlmRsi = hlmRsi10; break;
            case 20:  hlmRsi = hlmRsi20; break;
            default: hlmRsi = null;
        }
        return hlmRsi;
    }

    public void setHlmRsi(BigDecimal hlmRsi) {
        this.hlmRsi = hlmRsi;
    }

    public BigDecimal getOhlcRsi(int days) {
        switch (days) {
            case 3:  ohlcRsi = ohlcRsi03; break;
            case 5:  ohlcRsi = ohlcRsi05; break;
            case 10:  ohlcRsi = ohlcRsi10; break;
            case 20:  ohlcRsi = ohlcRsi20; break;
            default: ohlcRsi = null;
        }
        return ohlcRsi;
    }

    public void setOhlcRsi(BigDecimal ohlcRsi) {
        this.ohlcRsi = ohlcRsi;
    }

}
