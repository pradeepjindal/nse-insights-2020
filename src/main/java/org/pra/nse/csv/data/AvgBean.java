package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pra.nse.util.LocalDateDeserializer;
import org.pra.nse.util.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AvgBean {

    private String symbol;
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal atpAvg05;
    private BigDecimal atpAvg10;
    private BigDecimal atpAvg15;
    private BigDecimal atpAvg20;

    private BigDecimal volumeAvg05;
    private BigDecimal volumeAvg10;
    private BigDecimal volumeAvg15;
    private BigDecimal volumeAvg20;

    private BigDecimal deliveryAvg05;
    private BigDecimal deliveryAvg10;
    private BigDecimal deliveryAvg15;
    private BigDecimal deliveryAvg20;

    private BigDecimal oiAvg05;
    private BigDecimal oiAvg10;
    private BigDecimal oiAvg15;
    private BigDecimal oiAvg20;


    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +

                "," + atpAvg05 +
                "," + atpAvg10 +
                "," + atpAvg15 +
                "," + atpAvg20 +

                "," + volumeAvg05 +
                "," + volumeAvg10 +
                "," + volumeAvg15 +
                "," + volumeAvg20 +

                "," + deliveryAvg05 +
                "," + deliveryAvg10 +
                "," + deliveryAvg15 +
                "," + deliveryAvg20 +

                "," + oiAvg05 +
                "," + oiAvg10 +
                "," + oiAvg15 +
                "," + oiAvg20;
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

    public BigDecimal getVolumeAvg05() {
        return volumeAvg05;
    }

    public void setVolumeAvg05(BigDecimal volumeAvg05) {
        this.volumeAvg05 = volumeAvg05;
    }

    public BigDecimal getVolumeAvg10() {
        return volumeAvg10;
    }

    public void setVolumeAvg10(BigDecimal volumeAvg10) {
        this.volumeAvg10 = volumeAvg10;
    }

    public BigDecimal getVolumeAvg15() {
        return volumeAvg15;
    }

    public void setVolumeAvg15(BigDecimal volumeAvg15) {
        this.volumeAvg15 = volumeAvg15;
    }

    public BigDecimal getVolumeAvg20() {
        return volumeAvg20;
    }

    public void setVolumeAvg20(BigDecimal volumeAvg20) {
        this.volumeAvg20 = volumeAvg20;
    }

    public BigDecimal getDeliveryAvg05() {
        return deliveryAvg05;
    }

    public void setDeliveryAvg05(BigDecimal deliveryAvg05) {
        this.deliveryAvg05 = deliveryAvg05;
    }

    public BigDecimal getDeliveryAvg10() {
        return deliveryAvg10;
    }

    public void setDeliveryAvg10(BigDecimal deliveryAvg10) {
        this.deliveryAvg10 = deliveryAvg10;
    }

    public BigDecimal getDeliveryAvg15() {
        return deliveryAvg15;
    }

    public void setDeliveryAvg15(BigDecimal deliveryAvg15) {
        this.deliveryAvg15 = deliveryAvg15;
    }

    public BigDecimal getDeliveryAvg20() {
        return deliveryAvg20;
    }

    public void setDeliveryAvg20(BigDecimal deliveryAvg20) {
        this.deliveryAvg20 = deliveryAvg20;
    }

    public BigDecimal getOiAvg05() {
        return oiAvg05;
    }

    public void setOiAvg05(BigDecimal oiAvg05) {
        this.oiAvg05 = oiAvg05;
    }

    public BigDecimal getOiAvg10() {
        return oiAvg10;
    }

    public void setOiAvg10(BigDecimal oiAvg10) {
        this.oiAvg10 = oiAvg10;
    }

    public BigDecimal getOiAvg15() {
        return oiAvg15;
    }

    public void setOiAvg15(BigDecimal oiAvg15) {
        this.oiAvg15 = oiAvg15;
    }

    public BigDecimal getOiAvg20() {
        return oiAvg20;
    }

    public void setOiAvg20(BigDecimal oiAvg20) {
        this.oiAvg20 = oiAvg20;
    }

}
