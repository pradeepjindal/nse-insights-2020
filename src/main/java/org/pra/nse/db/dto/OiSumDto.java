package org.pra.nse.db.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OiSumDto {
    private String symbol;
    private LocalDate tradeDate;
    private BigDecimal sumOi;



    public String toCsvString() {
        return  symbol + ","
                + tradeDate + ","
                + sumOi;
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

    public BigDecimal getSumOi() {
        return sumOi;
    }

    public void setSumOi(BigDecimal sumOi) {
        this.sumOi = sumOi;
    }

}
