package org.pra.nse.db.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Calc_Avg_Tab")
public class CalcAvgTab implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_avg_seq", sequenceName = "calc_avg_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_avg_seq")
    private Long id;
    private String symbol;
    private LocalDate tradeDate;

    @Column(name = "atp_avg_05_Sma")
    private BigDecimal atpAvg05Sma;
    @Column(name = "atp_avg_10_Sma")
    private BigDecimal atpAvg10Sma;
    @Column(name = "atp_avg_15_Sma")
    private BigDecimal atpAvg15Sma;
    @Column(name = "atp_avg_20_Sma")
    private BigDecimal atpAvg20Sma;

    @Column(name = "vol_avg_05_Sma")
    private BigDecimal volAvg05Sma;
    @Column(name = "vol_avg_10_Sma")
    private BigDecimal volAvg10Sma;
    @Column(name = "vol_avg_15_Sma")
    private BigDecimal volAvg15Sma;
    @Column(name = "vol_avg_20_Sma")
    private BigDecimal volAvg20Sma;

    @Column(name = "del_avg_05_Sma")
    private BigDecimal delAvg05Sma;
    @Column(name = "del_avg_10_Sma")
    private BigDecimal delAvg10Sma;
    @Column(name = "del_avg_15_Sma")
    private BigDecimal delAvg15Sma;
    @Column(name = "del_avg_20_Sma")
    private BigDecimal delAvg20Sma;

    @Column(name = "oi_avg_05_Sma")
    private BigDecimal oiAvg05Sma;
    @Column(name = "oi_avg_10_Sma")
    private BigDecimal oiAvg10Sma;
    @Column(name = "oi_avg_15_Sma")
    private BigDecimal oiAvg15Sma;
    @Column(name = "oi_avg_20_Sma")
    private BigDecimal oiAvg20Sma;

//    public String toCsvString() {
//        return symbol +
//                "," + tradeDate +
//
//                "," + atpAvg05Sma +
//                "," + atpAvg10Sma +
//                "," + atpAvg15Sma +
//                "," + atpAvg20Sma +
//
//                "," + volAvg05Sma +
//                "," + volAvg10Sma +
//                "," + volAvg15Sma +
//                "," + volAvg20Sma +
//
//                "," + delAvg05Sma +
//                "," + delAvg10Sma +
//                "," + delAvg15Sma +
//                "," + delAvg20Sma +
//
//                "," + oiAvg05Sma +
//                "," + oiAvg10Sma +
//                "," + oiAvg15Sma +
//                "," + oiAvg20Sma;
//    }

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;

        atpAvg05Sma = null;
        atpAvg10Sma = null;
        atpAvg15Sma = null;
        atpAvg20Sma = null;

        volAvg05Sma = null;
        volAvg10Sma = null;
        volAvg15Sma = null;
        volAvg20Sma = null;

        delAvg05Sma = null;
        delAvg10Sma = null;
        delAvg15Sma = null;
        delAvg20Sma = null;

        oiAvg05Sma = null;
        oiAvg10Sma = null;
        oiAvg15Sma = null;
        oiAvg20Sma = null;
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

    public BigDecimal getAtpAvg05Sma() {
        return atpAvg05Sma;
    }

    public void setAtpAvg05Sma(BigDecimal atpAvg05Sma) {
        this.atpAvg05Sma = atpAvg05Sma;
    }

    public BigDecimal getAtpAvg10Sma() {
        return atpAvg10Sma;
    }

    public void setAtpAvg10Sma(BigDecimal atpAvg10Sma) {
        this.atpAvg10Sma = atpAvg10Sma;
    }

    public BigDecimal getAtpAvg15Sma() {
        return atpAvg15Sma;
    }

    public void setAtpAvg15Sma(BigDecimal atpAvg15Sma) {
        this.atpAvg15Sma = atpAvg15Sma;
    }

    public BigDecimal getAtpAvg20Sma() {
        return atpAvg20Sma;
    }

    public void setAtpAvg20Sma(BigDecimal atpAvg20Sma) {
        this.atpAvg20Sma = atpAvg20Sma;
    }

    public BigDecimal getVolAvg05Sma() {
        return volAvg05Sma;
    }

    public void setVolAvg05Sma(BigDecimal volAvg05Sma) {
        this.volAvg05Sma = volAvg05Sma;
    }

    public BigDecimal getVolAvg10Sma() {
        return volAvg10Sma;
    }

    public void setVolAvg10Sma(BigDecimal volAvg10Sma) {
        this.volAvg10Sma = volAvg10Sma;
    }

    public BigDecimal getVolAvg15Sma() {
        return volAvg15Sma;
    }

    public void setVolAvg15Sma(BigDecimal volAvg15Sma) {
        this.volAvg15Sma = volAvg15Sma;
    }

    public BigDecimal getVolAvg20Sma() {
        return volAvg20Sma;
    }

    public void setVolAvg20Sma(BigDecimal volAvg20Sma) {
        this.volAvg20Sma = volAvg20Sma;
    }

    public BigDecimal getDelAvg05Sma() {
        return delAvg05Sma;
    }

    public void setDelAvg05Sma(BigDecimal delAvg05Sma) {
        this.delAvg05Sma = delAvg05Sma;
    }

    public BigDecimal getDelAvg10Sma() {
        return delAvg10Sma;
    }

    public void setDelAvg10Sma(BigDecimal delAvg10Sma) {
        this.delAvg10Sma = delAvg10Sma;
    }

    public BigDecimal getDelAvg15Sma() {
        return delAvg15Sma;
    }

    public void setDelAvg15Sma(BigDecimal delAvg15Sma) {
        this.delAvg15Sma = delAvg15Sma;
    }

    public BigDecimal getDelAvg20Sma() {
        return delAvg20Sma;
    }

    public void setDelAvg20Sma(BigDecimal delAvg20Sma) {
        this.delAvg20Sma = delAvg20Sma;
    }

    public BigDecimal getOiAvg05Sma() {
        return oiAvg05Sma;
    }

    public void setOiAvg05Sma(BigDecimal oiAvg05Sma) {
        this.oiAvg05Sma = oiAvg05Sma;
    }

    public BigDecimal getOiAvg10Sma() {
        return oiAvg10Sma;
    }

    public void setOiAvg10Sma(BigDecimal oiAvg10Sma) {
        this.oiAvg10Sma = oiAvg10Sma;
    }

    public BigDecimal getOiAvg15Sma() {
        return oiAvg15Sma;
    }

    public void setOiAvg15Sma(BigDecimal oiAvg15Sma) {
        this.oiAvg15Sma = oiAvg15Sma;
    }

    public BigDecimal getOiAvg20Sma() {
        return oiAvg20Sma;
    }

    public void setOiAvg20Sma(BigDecimal oiAvg20Sma) {
        this.oiAvg20Sma = oiAvg20Sma;
    }

}
