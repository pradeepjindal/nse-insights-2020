package org.pra.nse.csv.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pra.nse.util.LocalDateDeserializer;
import org.pra.nse.util.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MfiBean {

    private String symbol;
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;

    private BigDecimal volumeAtpMfi05;
    private BigDecimal volumeAtpMfi10;
    private BigDecimal volumeAtpMfi15;
    private BigDecimal volumeAtpMfi20;

    private BigDecimal deliveryAtpMfi05;
    private BigDecimal deliveryAtpMfi10;
    private BigDecimal deliveryAtpMfi15;
    private BigDecimal deliveryAtpMfi20;


    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +

                "," + volumeAtpMfi05 +
                "," + volumeAtpMfi10 +
                "," + volumeAtpMfi15 +
                "," + volumeAtpMfi20 +

                "," + deliveryAtpMfi05 +
                "," + deliveryAtpMfi10 +
                "," + deliveryAtpMfi15 +
                "," + deliveryAtpMfi20;
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

    public BigDecimal getVolumeAtpMfi05() {
        return volumeAtpMfi05;
    }

    public void setVolumeAtpMfi05(BigDecimal volumeAtpMfi05) {
        this.volumeAtpMfi05 = volumeAtpMfi05;
    }

    public BigDecimal getVolumeAtpMfi10() {
        return volumeAtpMfi10;
    }

    public void setVolumeAtpMfi10(BigDecimal volumeAtpMfi10) {
        this.volumeAtpMfi10 = volumeAtpMfi10;
    }

    public BigDecimal getVolumeAtpMfi15() {
        return volumeAtpMfi15;
    }

    public void setVolumeAtpMfi15(BigDecimal volumeAtpMfi15) {
        this.volumeAtpMfi15 = volumeAtpMfi15;
    }

    public BigDecimal getVolumeAtpMfi20() {
        return volumeAtpMfi20;
    }

    public void setVolumeAtpMfi20(BigDecimal volumeAtpMfi20) {
        this.volumeAtpMfi20 = volumeAtpMfi20;
    }

    public BigDecimal getDeliveryAtpMfi05() {
        return deliveryAtpMfi05;
    }

    public void setDeliveryAtpMfi05(BigDecimal deliveryAtpMfi05) {
        this.deliveryAtpMfi05 = deliveryAtpMfi05;
    }

    public BigDecimal getDeliveryAtpMfi10() {
        return deliveryAtpMfi10;
    }

    public void setDeliveryAtpMfi10(BigDecimal deliveryAtpMfi10) {
        this.deliveryAtpMfi10 = deliveryAtpMfi10;
    }

    public BigDecimal getDeliveryAtpMfi15() {
        return deliveryAtpMfi15;
    }

    public void setDeliveryAtpMfi15(BigDecimal deliveryAtpMfi15) {
        this.deliveryAtpMfi15 = deliveryAtpMfi15;
    }

    public BigDecimal getDeliveryAtpMfi20() {
        return deliveryAtpMfi20;
    }

    public void setDeliveryAtpMfi20(BigDecimal deliveryAtpMfi20) {
        this.deliveryAtpMfi20 = deliveryAtpMfi20;
    }

}
