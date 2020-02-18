package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AvgBean implements CalcBean {

    private String symbol;
    //@JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal atpAvg;
    private BigDecimal volAvg;
    private BigDecimal delAvg;
    private BigDecimal foiAvg;


    private BigDecimal atpAvg03;
    private BigDecimal atpAvg05;
    private BigDecimal atpAvg10;
    private BigDecimal atpAvg15;
    private BigDecimal atpAvg20;

    private BigDecimal volAvg03;
    private BigDecimal volAvg05;
    private BigDecimal volAvg10;
    private BigDecimal volAvg15;
    private BigDecimal volAvg20;

    private BigDecimal delAvg03;
    private BigDecimal delAvg05;
    private BigDecimal delAvg10;
    private BigDecimal delAvg15;
    private BigDecimal delAvg20;

    private BigDecimal foiAvg03;
    private BigDecimal foiAvg05;
    private BigDecimal foiAvg10;
    private BigDecimal foiAvg15;
    private BigDecimal foiAvg20;


    public String toCsvString() {
        return symbol +
                "," + tradeDate +

                "," + atpAvg03 +
                "," + atpAvg05 +
                "," + atpAvg10 +
                "," + atpAvg15 +
                "," + atpAvg20 +

                "," + volAvg03 +
                "," + volAvg05 +
                "," + volAvg10 +
                "," + volAvg15 +
                "," + volAvg20 +

                "," + delAvg03 +
                "," + delAvg05 +
                "," + delAvg10 +
                "," + delAvg15 +
                "," + delAvg20 +

                "," + foiAvg03 +
                "," + foiAvg05 +
                "," + foiAvg10 +
                "," + foiAvg15 +
                "," + foiAvg20;
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

    public BigDecimal getAtpAvg05() {
        return atpAvg05;
    }

    public void setAtpAvg05(BigDecimal atpAvg05) {
        this.atpAvg05 = atpAvg05;
    }

    public BigDecimal getAtpAvg10() {
        return atpAvg10;
    }

    public void setAtpAvg10(BigDecimal atpAvg10) {
        this.atpAvg10 = atpAvg10;
    }

    public BigDecimal getAtpAvg15() {
        return atpAvg15;
    }

    public void setAtpAvg15(BigDecimal atpAvg15) {
        this.atpAvg15 = atpAvg15;
    }

    public BigDecimal getAtpAvg20() {
        return atpAvg20;
    }

    public void setAtpAvg20(BigDecimal atpAvg20) {
        this.atpAvg20 = atpAvg20;
    }

    public BigDecimal getVolAvg05() {
        return volAvg05;
    }

    public void setVolAvg05(BigDecimal volAvg05) {
        this.volAvg05 = volAvg05;
    }

    public BigDecimal getVolAvg10() {
        return volAvg10;
    }

    public void setVolAvg10(BigDecimal volAvg10) {
        this.volAvg10 = volAvg10;
    }

    public BigDecimal getVolAvg15() {
        return volAvg15;
    }

    public void setVolAvg15(BigDecimal volAvg15) {
        this.volAvg15 = volAvg15;
    }

    public BigDecimal getVolAvg20() {
        return volAvg20;
    }

    public void setVolAvg20(BigDecimal volAvg20) {
        this.volAvg20 = volAvg20;
    }

    public BigDecimal getDelAvg05() {
        return delAvg05;
    }

    public void setDelAvg05(BigDecimal delAvg05) {
        this.delAvg05 = delAvg05;
    }

    public BigDecimal getDelAvg10() {
        return delAvg10;
    }

    public void setDelAvg10(BigDecimal delAvg10) {
        this.delAvg10 = delAvg10;
    }

    public BigDecimal getDelAvg15() {
        return delAvg15;
    }

    public void setDelAvg15(BigDecimal delAvg15) {
        this.delAvg15 = delAvg15;
    }

    public BigDecimal getDelAvg20() {
        return delAvg20;
    }

    public void setDelAvg20(BigDecimal delAvg20) {
        this.delAvg20 = delAvg20;
    }

    public BigDecimal getFoiAvg05() {
        return foiAvg05;
    }

    public void setFoiAvg05(BigDecimal foiAvg05) {
        this.foiAvg05 = foiAvg05;
    }

    public BigDecimal getFoiAvg10() {
        return foiAvg10;
    }

    public void setFoiAvg10(BigDecimal foiAvg10) {
        this.foiAvg10 = foiAvg10;
    }

    public BigDecimal getFoiAvg15() {
        return foiAvg15;
    }

    public void setFoiAvg15(BigDecimal foiAvg15) {
        this.foiAvg15 = foiAvg15;
    }

    public BigDecimal getFoiAvg20() {
        return foiAvg20;
    }

    public void setFoiAvg20(BigDecimal foiAvg20) {
        this.foiAvg20 = foiAvg20;
    }

    public BigDecimal getAtpAvg() {
        return atpAvg;
    }

    public BigDecimal getAtpAvg03() {
        return atpAvg03;
    }

    public void setAtpAvg03(BigDecimal atpAvg03) {
        this.atpAvg03 = atpAvg03;
    }

    public BigDecimal getVolAvg03() {
        return volAvg03;
    }

    public void setVolAvg03(BigDecimal volAvg03) {
        this.volAvg03 = volAvg03;
    }

    public BigDecimal getDelAvg03() {
        return delAvg03;
    }

    public void setDelAvg03(BigDecimal delAvg03) {
        this.delAvg03 = delAvg03;
    }

    public BigDecimal getFoiAvg03() {
        return foiAvg03;
    }

    public void setFoiAvg03(BigDecimal foiAvg03) {
        this.foiAvg03 = foiAvg03;
    }



    public BigDecimal getAtpAvg(int days) {
        switch (days) {
            case 3:  atpAvg = atpAvg03; break;
            case 5:  atpAvg = atpAvg05; break;
            case 10:  atpAvg = atpAvg10; break;
            case 20:  atpAvg = atpAvg20; break;
            default: atpAvg = null;
        }
        return atpAvg;
    }

    public void setAtpAvg(BigDecimal atpAvg) {
        this.atpAvg = atpAvg;
    }

    public BigDecimal getVolAvg(int days) {
        switch (days) {
            case 3:  volAvg = volAvg03; break;
            case 5:  volAvg = volAvg05; break;
            case 10:  volAvg = volAvg10; break;
            case 20:  volAvg = volAvg20; break;
            default: volAvg = null;
        }
        return volAvg;
    }

    public void setVolAvg(BigDecimal volAvg) {
        this.volAvg = volAvg;
    }

    public BigDecimal getDelAvg(int days) {
        switch (days) {
            case 3:  delAvg = delAvg03; break;
            case 5:  delAvg = delAvg05; break;
            case 10:  delAvg = delAvg10; break;
            case 20:  delAvg = delAvg20; break;
            default: delAvg = null;
        }
        return delAvg;
    }

    public void setDelAvg(BigDecimal delAvg) {
        this.delAvg = delAvg;
    }

    public BigDecimal getFoiAvg(int days) {
        switch (days) {
            case 3:  foiAvg = foiAvg03; break;
            case 5:  foiAvg = foiAvg05; break;
            case 10:  foiAvg = foiAvg10; break;
            case 20:  foiAvg = foiAvg20; break;
            default: foiAvg = null;
        }
        return foiAvg;
    }

    public void setFoiAvg(BigDecimal foiAvg) {
        this.foiAvg = foiAvg;
    }

}
