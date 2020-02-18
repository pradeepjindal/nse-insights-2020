package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MfiBean implements CalcBean {

    private String symbol;
    //@JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal volMfi;
    private BigDecimal delMfi;


    private BigDecimal volMfi03;
    private BigDecimal volMfi05;
    private BigDecimal volMfi10;
    private BigDecimal volMfi15;
    private BigDecimal volMfi20;

    private BigDecimal delMfi03;
    private BigDecimal delMfi05;
    private BigDecimal delMfi10;
    private BigDecimal delMfi15;
    private BigDecimal delMfi20;


    public String toCsvString() {
        return symbol +
                "," + tradeDate +

                "," + volMfi03 +
                "," + volMfi05 +
                "," + volMfi10 +
                "," + volMfi15 +
                "," + volMfi20 +

                "," + delMfi03 +
                "," + delMfi05 +
                "," + delMfi10 +
                "," + delMfi15 +
                "," + delMfi20;
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

    public BigDecimal getVolMfi05() {
        return volMfi05;
    }

    public void setVolMfi05(BigDecimal volMfi05) {
        this.volMfi05 = volMfi05;
    }

    public BigDecimal getVolMfi10() {
        return volMfi10;
    }

    public void setVolMfi10(BigDecimal volMfi10) {
        this.volMfi10 = volMfi10;
    }

    public BigDecimal getVolMfi15() {
        return volMfi15;
    }

    public void setVolMfi15(BigDecimal volMfi15) {
        this.volMfi15 = volMfi15;
    }

    public BigDecimal getVolMfi20() {
        return volMfi20;
    }

    public void setVolMfi20(BigDecimal volMfi20) {
        this.volMfi20 = volMfi20;
    }

    public BigDecimal getDelMfi05() {
        return delMfi05;
    }

    public void setDelMfi05(BigDecimal delMfi05) {
        this.delMfi05 = delMfi05;
    }

    public BigDecimal getDelMfi10() {
        return delMfi10;
    }

    public void setDelMfi10(BigDecimal delMfi10) {
        this.delMfi10 = delMfi10;
    }

    public BigDecimal getDelMfi15() {
        return delMfi15;
    }

    public void setDelMfi15(BigDecimal delMfi15) {
        this.delMfi15 = delMfi15;
    }

    public BigDecimal getDelMfi20() {
        return delMfi20;
    }

    public void setDelMfi20(BigDecimal delMfi20) {
        this.delMfi20 = delMfi20;
    }


    public BigDecimal getDelMfi03() {
        return delMfi03;
    }

    public void setDelMfi03(BigDecimal delMfi03) {
        this.delMfi03 = delMfi03;
    }

    public BigDecimal getVolMfi03() {
        return volMfi03;
    }

    public void setVolMfi03(BigDecimal volMfi03) {
        this.volMfi03 = volMfi03;
    }



    public BigDecimal getDelMfi(int days) {
        switch (days) {
            case 3:  delMfi = delMfi03; break;
            case 5:  delMfi = delMfi05; break;
            case 10:  delMfi = delMfi10; break;
            case 20:  delMfi = delMfi20; break;
            default: delMfi = null;
        }
        return delMfi;
    }

    public void setDelMfi(BigDecimal delMfi) {
        this.delMfi = delMfi;
    }

    public BigDecimal getVolMfi(int days) {
        switch (days) {
            case 3:  volMfi = volMfi03; break;
            case 5:  volMfi = volMfi05; break;
            case 10:  volMfi = volMfi10; break;
            case 20:  volMfi = volMfi20; break;
            default: volMfi = null;
        }
        return volMfi;
    }

    public void setVolMfi(BigDecimal volMfi) {
        this.volMfi = volMfi;
    }

}
