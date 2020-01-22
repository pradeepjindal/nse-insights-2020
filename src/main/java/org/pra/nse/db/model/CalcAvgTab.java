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

    @Column(name = "volume_avg_05")
    private BigDecimal volumeAvg05;
    @Column(name = "volume_avg_10")
    private BigDecimal volumeAvg10;
    @Column(name = "volume_avg_15")
    private BigDecimal volumeAvg15;
    @Column(name = "volume_avg_20")
    private BigDecimal volumeAvg20;

    @Column(name = "delivery_avg_05")
    private BigDecimal deliveryAvg05;
    @Column(name = "delivery_avg_10")
    private BigDecimal deliveryAvg10;
    @Column(name = "delivery_avg_15")
    private BigDecimal deliveryAvg15;
    @Column(name = "delivery_avg_20")
    private BigDecimal deliveryAvg20;

    @Column(name = "oi_avg_05")
    private BigDecimal oiAvg05;
    @Column(name = "oi_avg_10")
    private BigDecimal oiAvg10;
    @Column(name = "oi_avg_15")
    private BigDecimal oiAvg15;
    @Column(name = "oi_avg_20")
    private BigDecimal oiAvg20;

    @Override
    public String toString() {
        return symbol +
                "," + tradeDate +

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

    public void reset() {
        id = null;
        symbol = null;
        tradeDate = null;

        volumeAvg05 = null;
        volumeAvg10 = null;
        volumeAvg15 = null;
        volumeAvg20 = null;

        deliveryAvg05 = null;
        deliveryAvg10 = null;
        deliveryAvg15 = null;
        deliveryAvg20 = null;

        oiAvg05 = null;
        oiAvg10 = null;
        oiAvg15 = null;
        oiAvg20 = null;
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
