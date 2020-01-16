package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pra.nse.util.LocalDateDeserializer;
import org.pra.nse.util.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RsiBean {

    private String symbol;
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;
    private BigDecimal openRsi10Ema;
    private BigDecimal highRsi10Ema;
    private BigDecimal lowRsi10Ema;
    private BigDecimal closeRsi10Ema;
    private BigDecimal lastRsi10Ema;
    private BigDecimal atpRsi10Ema;
    private BigDecimal hlmRsi10Ema;


    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +
                "," + openRsi10Ema +
                "," + highRsi10Ema +
                "," + lowRsi10Ema +
                "," + closeRsi10Ema +
                "," + lastRsi10Ema +
                "," + atpRsi10Ema +
                "," + hlmRsi10Ema;
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

    public BigDecimal getOpenRsi10Ema() {
        return openRsi10Ema;
    }

    public void setOpenRsi10Ema(BigDecimal openRsi10Ema) {
        this.openRsi10Ema = openRsi10Ema;
    }

    public BigDecimal getHighRsi10Ema() {
        return highRsi10Ema;
    }

    public void setHighRsi10Ema(BigDecimal highRsi10Ema) {
        this.highRsi10Ema = highRsi10Ema;
    }

    public BigDecimal getLowRsi10Ema() {
        return lowRsi10Ema;
    }

    public void setLowRsi10Ema(BigDecimal lowRsi10Ema) {
        this.lowRsi10Ema = lowRsi10Ema;
    }

    public BigDecimal getCloseRsi10Ema() {
        return closeRsi10Ema;
    }

    public void setCloseRsi10Ema(BigDecimal closeRsi10Ema) {
        this.closeRsi10Ema = closeRsi10Ema;
    }

    public BigDecimal getLastRsi10Ema() {
        return lastRsi10Ema;
    }

    public void setLastRsi10Ema(BigDecimal lastRsi10Ema) {
        this.lastRsi10Ema = lastRsi10Ema;
    }

    public BigDecimal getAtpRsi10Ema() {
        return atpRsi10Ema;
    }

    public void setAtpRsi10Ema(BigDecimal atpRsi10Ema) {
        this.atpRsi10Ema = atpRsi10Ema;
    }

    public BigDecimal getHlmRsi10Ema() {
        return hlmRsi10Ema;
    }

    public void setHlmRsi10Ema(BigDecimal highLowMidRsi10Ema) {
        this.hlmRsi10Ema = highLowMidRsi10Ema;
    }


}
