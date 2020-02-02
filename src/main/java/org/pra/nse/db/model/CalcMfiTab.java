package org.pra.nse.db.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Calc_Mfi_Tab")
public class CalcMfiTab implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @SequenceGenerator(name = "calc_mfi_seq", sequenceName = "calc_mfi_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calc_mfi_seq")
    private Long id;
    private String symbol;
    private LocalDate tradeDate;

    @Column(name = "vol_atp_mfi_05_Sma")
    private BigDecimal volAtpMfi05Sma;
    @Column(name = "vol_atp_mfi_10_Sma")
    private BigDecimal volAtpMfi10Sma;
    @Column(name = "vol_atp_mfi_15_Sma")
    private BigDecimal volAtpMfi15Sma;
    @Column(name = "vol_atp_mfi_20_Sma")
    private BigDecimal volAtpMfi20Sma;

    @Column(name = "del_atp_mfi_05_Sma")
    private BigDecimal delAtpMfi05Sma;
    @Column(name = "del_atp_mfi_10_Sma")
    private BigDecimal delAtpMfi10Sma;
    @Column(name = "del_atp_mfi_15_Sma")
    private BigDecimal delAtpMfi15Sma;
    @Column(name = "del_atp_mfi_20_Sma")
    private BigDecimal delAtpMfi20Sma;



    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +

                "," + volAtpMfi05Sma +
                "," + volAtpMfi10Sma +
                "," + volAtpMfi15Sma +
                "," + volAtpMfi20Sma +

                "," + delAtpMfi05Sma +
                "," + delAtpMfi10Sma +
                "," + delAtpMfi15Sma +
                "," + delAtpMfi20Sma;
    }

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;

        volAtpMfi05Sma = null;
        volAtpMfi10Sma = null;
        volAtpMfi15Sma = null;
        volAtpMfi20Sma = null;

        delAtpMfi05Sma = null;
        delAtpMfi10Sma = null;
        delAtpMfi15Sma = null;
        delAtpMfi20Sma = null;
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

    public BigDecimal getVolAtpMfi05Sma() {
        return volAtpMfi05Sma;
    }

    public void setVolAtpMfi05Sma(BigDecimal volAtpMfi05Sma) {
        this.volAtpMfi05Sma = volAtpMfi05Sma;
    }

    public BigDecimal getVolAtpMfi10Sma() {
        return volAtpMfi10Sma;
    }

    public void setVolAtpMfi10Sma(BigDecimal volAtpMfi10Sma) {
        this.volAtpMfi10Sma = volAtpMfi10Sma;
    }

    public BigDecimal getVolAtpMfi15Sma() {
        return volAtpMfi15Sma;
    }

    public void setVolAtpMfi15Sma(BigDecimal volAtpMfi15Sma) {
        this.volAtpMfi15Sma = volAtpMfi15Sma;
    }

    public BigDecimal getVolAtpMfi20Sma() {
        return volAtpMfi20Sma;
    }

    public void setVolAtpMfi20Sma(BigDecimal volAtpMfi20Sma) {
        this.volAtpMfi20Sma = volAtpMfi20Sma;
    }

    public BigDecimal getDelAtpMfi05Sma() {
        return delAtpMfi05Sma;
    }

    public void setDelAtpMfi05Sma(BigDecimal delAtpMfi05Sma) {
        this.delAtpMfi05Sma = delAtpMfi05Sma;
    }

    public BigDecimal getDelAtpMfi10Sma() {
        return delAtpMfi10Sma;
    }

    public void setDelAtpMfi10Sma(BigDecimal delAtpMfi10Sma) {
        this.delAtpMfi10Sma = delAtpMfi10Sma;
    }

    public BigDecimal getDelAtpMfi15Sma() {
        return delAtpMfi15Sma;
    }

    public void setDelAtpMfi15Sma(BigDecimal delAtpMfi15Sma) {
        this.delAtpMfi15Sma = delAtpMfi15Sma;
    }

    public BigDecimal getDelAtpMfi20Sma() {
        return delAtpMfi20Sma;
    }

    public void setDelAtpMfi20Sma(BigDecimal delAtpMfi20Sma) {
        this.delAtpMfi20Sma = delAtpMfi20Sma;
    }
}
