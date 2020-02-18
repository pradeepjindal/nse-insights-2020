package org.pra.nse.db.entity.mfi;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MfiTab {

    public Long getId();
    public void setId(Long id);

    String getSymbol();
    void setSymbol(String symbol);

    LocalDate getTradeDate();
    void setTradeDate(LocalDate tradeDate);

    BigDecimal getVolAtpMfiSma();
    void setVolAtpMfiSma(BigDecimal volAtpMfiSma);

    BigDecimal getDelAtpMfiSma();
    void setDelAtpMfiSma(BigDecimal delAtpMfiSma);

}
