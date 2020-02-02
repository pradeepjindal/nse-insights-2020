package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pra.nse.util.LocalDateDeserializer;
import org.pra.nse.util.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RsiBean implements CalcBean {

    private String symbol;
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal openRsi10;
    private BigDecimal highRsi10;
    private BigDecimal lowRsi10;
    private BigDecimal closeRsi10;
    private BigDecimal lastRsi10;
    private BigDecimal atpRsi10;
    private BigDecimal hlmRsi10;

    private BigDecimal openRsi20;
    private BigDecimal highRsi20;
    private BigDecimal lowRsi20;
    private BigDecimal closeRsi20;
    private BigDecimal lastRsi20;
    private BigDecimal atpRsi20;
    private BigDecimal hlmRsi20;


    public String toCsvString() {
        return symbol +
                "," + tradeDate +

                "," + openRsi10 +
                "," + highRsi10 +
                "," + lowRsi10 +
                "," + closeRsi10 +
                "," + lastRsi10 +
                "," + atpRsi10 +
                "," + hlmRsi10 +

                "," + openRsi20 +
                "," + highRsi20 +
                "," + lowRsi20 +
                "," + closeRsi20 +
                "," + lastRsi20 +
                "," + atpRsi20 +
                "," + hlmRsi20;
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
}
