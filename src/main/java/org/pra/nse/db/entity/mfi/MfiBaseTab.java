package org.pra.nse.db.entity.mfi;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

abstract class MfiBaseTab implements MfiTab {

    private String symbol;
    private LocalDate tradeDate;

    @Column(name = "vol_atp_mfi_sma")
    private BigDecimal volAtpMfiSma;

    @Column(name = "del_atp_mfi_sma")
    private BigDecimal delAtpMfiSma;


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

    public BigDecimal getVolAtpMfiSma() {
        return volAtpMfiSma;
    }

    public void setVolAtpMfiSma(BigDecimal volAtpMfiSma) {
        this.volAtpMfiSma = volAtpMfiSma;
    }

    public BigDecimal getDelAtpMfiSma() {
        return delAtpMfiSma;
    }

    public void setDelAtpMfiSma(BigDecimal delAtpMfiSma) {
        this.delAtpMfiSma = delAtpMfiSma;
    }

}
