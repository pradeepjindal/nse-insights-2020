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

    @Column(name = "open_rsi_05_Sma")
    private BigDecimal openRsi05Sma;
    @Column(name = "high_rsi_05_Sma")
    private BigDecimal highRsi05Sma;
    @Column(name = "low_rsi_05_Sma")
    private BigDecimal lowRsi05Sma;
    @Column(name = "close_rsi_05_Sma")
    private BigDecimal closeRsi05Sma;
    @Column(name = "last_rsi_05_Sma")
    private BigDecimal lastRsi05Sma;
    @Column(name = "atp_rsi_05_Sma")
    private BigDecimal atpRsi05Sma;
    @Column(name = "hlm_rsi_05_Sma")
    private BigDecimal hlmRsi05Sma;
    @Column(name = "ohlc_rsi_05_Sma")
    private BigDecimal ohlcRsi05Sma;

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
    @Column(name = "ohlc_rsi_10_Sma")
    private BigDecimal ohlcRsi10Sma;

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
    @Column(name = "ohlc_rsi_20_Sma")
    private BigDecimal ohlcRsi20Sma;

//    public String toCsvString() {
//        return symbol +
//                "," + tradeDate +
//
//                "," + openRsi10Sma +
//                "," + highRsi10Sma +
//                "," + lowRsi10Sma +
//                "," + closeRsi10Sma +
//                "," + lastRsi10Sma +
//                "," + atpRsi10Sma +
//                "," + hlmRsi10Sma +
//
//                "," + openRsi20Sma +
//                "," + highRsi20Sma +
//                "," + lowRsi20Sma +
//                "," + closeRsi20Sma +
//                "," + lastRsi20Sma +
//                "," + atpRsi20Sma +
//                "," + hlmRsi20Sma;
//    }

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;

        openRsi05Sma = null;
        highRsi05Sma = null;
        lowRsi05Sma = null;
        closeRsi05Sma = null;
        lastRsi05Sma = null;
        atpRsi05Sma = null;
        hlmRsi05Sma = null;
        ohlcRsi05Sma = null;

        openRsi10Sma = null;
        highRsi10Sma = null;
        lowRsi10Sma = null;
        closeRsi10Sma = null;
        lastRsi10Sma = null;
        atpRsi10Sma = null;
        hlmRsi10Sma = null;
        ohlcRsi10Sma = null;

        openRsi20Sma = null;
        highRsi20Sma = null;
        lowRsi20Sma = null;
        closeRsi20Sma = null;
        lastRsi20Sma = null;
        atpRsi20Sma = null;
        hlmRsi20Sma = null;
        ohlcRsi20Sma = null;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public BigDecimal getOpenRsi05Sma() {
        return openRsi05Sma;
    }

    public void setOpenRsi05Sma(BigDecimal openRsi05Sma) {
        this.openRsi05Sma = openRsi05Sma;
    }

    public BigDecimal getHighRsi05Sma() {
        return highRsi05Sma;
    }

    public void setHighRsi05Sma(BigDecimal highRsi05Sma) {
        this.highRsi05Sma = highRsi05Sma;
    }

    public BigDecimal getLowRsi05Sma() {
        return lowRsi05Sma;
    }

    public void setLowRsi05Sma(BigDecimal lowRsi05Sma) {
        this.lowRsi05Sma = lowRsi05Sma;
    }

    public BigDecimal getCloseRsi05Sma() {
        return closeRsi05Sma;
    }

    public void setCloseRsi05Sma(BigDecimal closeRsi05Sma) {
        this.closeRsi05Sma = closeRsi05Sma;
    }

    public BigDecimal getLastRsi05Sma() {
        return lastRsi05Sma;
    }

    public void setLastRsi05Sma(BigDecimal lastRsi05Sma) {
        this.lastRsi05Sma = lastRsi05Sma;
    }

    public BigDecimal getAtpRsi05Sma() {
        return atpRsi05Sma;
    }

    public void setAtpRsi05Sma(BigDecimal atpRsi05Sma) {
        this.atpRsi05Sma = atpRsi05Sma;
    }

    public BigDecimal getHlmRsi05Sma() {
        return hlmRsi05Sma;
    }

    public void setHlmRsi05Sma(BigDecimal hlmRsi05Sma) {
        this.hlmRsi05Sma = hlmRsi05Sma;
    }

    public BigDecimal getOhlcRsi05Sma() {
        return ohlcRsi05Sma;
    }

    public void setOhlcRsi05Sma(BigDecimal ohlcRsi05Sma) {
        this.ohlcRsi05Sma = ohlcRsi05Sma;
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

    public BigDecimal getOhlcRsi10Sma() {
        return ohlcRsi10Sma;
    }

    public void setOhlcRsi10Sma(BigDecimal ohlcRsi10Sma) {
        this.ohlcRsi10Sma = ohlcRsi10Sma;
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

    public BigDecimal getOhlcRsi20Sma() {
        return ohlcRsi20Sma;
    }

    public void setOhlcRsi20Sma(BigDecimal ohlcRsi20Sma) {
        this.ohlcRsi20Sma = ohlcRsi20Sma;
    }

}
