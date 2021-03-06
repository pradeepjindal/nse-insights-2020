package org.pra.nse.csv.data;

import java.time.LocalDate;

public interface CalcBean {
    public String getSymbol();
    public void setSymbol(String symbol);

    public LocalDate getTradeDate();
    public void setTradeDate(LocalDate tradeDate);

    public String toCsvString();
}
