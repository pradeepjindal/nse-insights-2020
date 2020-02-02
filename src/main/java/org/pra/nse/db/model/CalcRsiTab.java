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

    @Column(name = "open_rsi_10_Sma")
    private BigDecimal openRsi10Sma;
    @Column(name = "high_rsi_10_Sma")
    private BigDecimal highRsi10Sma;
    @Column(name = "low_rsi_10_Sma")
    private BigDecimal lowRsi10Sma;
    @Column(name = "close_rsi_10_Sma")
    private BigDecimal closeRsi10Sma;
    @Column(name = "last_rsi_10_Sma")
    private BigDecimal lastRsi10Sma;
    @Column(name = "atp_rsi_10_Sma")
    private BigDecimal atpRsi10Sma;
    @Column(name = "hlm_rsi_10_Sma")
    private BigDecimal hlmRsi10Sma;

    @Column(name = "open_rsi_20_Sma")
    private BigDecimal openRsi20Sma;
    @Column(name = "high_rsi_20_Sma")
    private BigDecimal highRsi20Sma;
    @Column(name = "low_rsi_20_Sma")
    private BigDecimal lowRsi20Sma;
    @Column(name = "close_rsi_20_Sma")
    private BigDecimal closeRsi20Sma;
    @Column(name = "last_rsi_20_Sma")
    private BigDecimal lastRsi20Sma;
    @Column(name = "atp_rsi_20_Sma")
    private BigDecimal atpRsi20Sma;
    @Column(name = "hlm_rsi_20_Sma")
    private BigDecimal hlmRsi20Sma;

    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +

                "," + openRsi10Sma +
                "," + highRsi10Sma +
                "," + lowRsi10Sma +
                "," + closeRsi10Sma +
                "," + lastRsi10Sma +
                "," + atpRsi10Sma +
                "," + hlmRsi10Sma +

                "," + openRsi20Sma +
                "," + highRsi20Sma +
                "," + lowRsi20Sma +
                "," + closeRsi20Sma +
                "," + lastRsi20Sma +
                "," + atpRsi20Sma +
                "," + hlmRsi20Sma;
    }

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;

        openRsi10Sma = null;
        highRsi10Sma = null;
        lowRsi10Sma = null;
        closeRsi10Sma = null;
        lastRsi10Sma = null;
        atpRsi10Sma = null;
        hlmRsi10Sma = null;

        openRsi20Sma = null;
        highRsi20Sma = null;
        lowRsi20Sma = null;
        closeRsi20Sma = null;
        lastRsi20Sma = null;
        atpRsi20Sma = null;
        hlmRsi20Sma = null;
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


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigDecimal getOpenRsi10Sma() {
        return openRsi10Sma;
    }

    public void setOpenRsi10Sma(BigDecimal openRsi10Sma) {
        this.openRsi10Sma = openRsi10Sma;
    }

    public BigDecimal getHighRsi10Sma() {
        return highRsi10Sma;
    }

    public void setHighRsi10Sma(BigDecimal highRsi10Sma) {
        this.highRsi10Sma = highRsi10Sma;
    }

    public BigDecimal getLowRsi10Sma() {
        return lowRsi10Sma;
    }

    public void setLowRsi10Sma(BigDecimal lowRsi10Sma) {
        this.lowRsi10Sma = lowRsi10Sma;
    }

    public BigDecimal getCloseRsi10Sma() {
        return closeRsi10Sma;
    }

    public void setCloseRsi10Sma(BigDecimal closeRsi10Sma) {
        this.closeRsi10Sma = closeRsi10Sma;
    }

    public BigDecimal getLastRsi10Sma() {
        return lastRsi10Sma;
    }

    public void setLastRsi10Sma(BigDecimal lastRsi10Sma) {
        this.lastRsi10Sma = lastRsi10Sma;
    }

    public BigDecimal getAtpRsi10Sma() {
        return atpRsi10Sma;
    }

    public void setAtpRsi10Sma(BigDecimal atpRsi10Sma) {
        this.atpRsi10Sma = atpRsi10Sma;
    }

    public BigDecimal getHlmRsi10Sma() {
        return hlmRsi10Sma;
    }

    public void setHlmRsi10Sma(BigDecimal hlmRsi10Sma) {
        this.hlmRsi10Sma = hlmRsi10Sma;
    }

    public BigDecimal getOpenRsi20Sma() {
        return openRsi20Sma;
    }

    public void setOpenRsi20Sma(BigDecimal openRsi20Sma) {
        this.openRsi20Sma = openRsi20Sma;
    }

    public BigDecimal getHighRsi20Sma() {
        return highRsi20Sma;
    }

    public void setHighRsi20Sma(BigDecimal highRsi20Sma) {
        this.highRsi20Sma = highRsi20Sma;
    }

    public BigDecimal getLowRsi20Sma() {
        return lowRsi20Sma;
    }

    public void setLowRsi20Sma(BigDecimal lowRsi20Sma) {
        this.lowRsi20Sma = lowRsi20Sma;
    }

    public BigDecimal getCloseRsi20Sma() {
        return closeRsi20Sma;
    }

    public void setCloseRsi20Sma(BigDecimal closeRsi20Sma) {
        this.closeRsi20Sma = closeRsi20Sma;
    }

    public BigDecimal getLastRsi20Sma() {
        return lastRsi20Sma;
    }

    public void setLastRsi20Sma(BigDecimal lastRsi20Sma) {
        this.lastRsi20Sma = lastRsi20Sma;
    }

    public BigDecimal getAtpRsi20Sma() {
        return atpRsi20Sma;
    }

    public void setAtpRsi20Sma(BigDecimal atpRsi20Sma) {
        this.atpRsi20Sma = atpRsi20Sma;
    }

    public BigDecimal getHlmRsi20Sma() {
        return hlmRsi20Sma;
    }

    public void setHlmRsi20Sma(BigDecimal hlmRsi20Sma) {
        this.hlmRsi20Sma = hlmRsi20Sma;
    }
}
