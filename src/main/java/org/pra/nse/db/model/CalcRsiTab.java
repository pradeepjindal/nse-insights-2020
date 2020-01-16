package org.pra.nse.db.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Calc_Rsi_Tab")
public class CalcRsiTab implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_rsi_seq", sequenceName = "calc_rsi_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_rsi_seq")
    private Long id;
    private String symbol;
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

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;
        openRsi10Ema = null;
        highRsi10Ema = null;
        lowRsi10Ema = null;
        closeRsi10Ema = null;
        lastRsi10Ema = null;
        atpRsi10Ema = null;
        hlmRsi10Ema = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
