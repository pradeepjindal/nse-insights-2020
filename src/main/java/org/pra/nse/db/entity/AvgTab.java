package org.pra.nse.db.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AvgTab {

    protected String symbol;
    protected LocalDate tradeDate;

    @Column(name = "atp_avg_sma")
    protected BigDecimal atpAvgSma;

    @Column(name = "vol_avg_sma")
    private BigDecimal volAvgSma;

    @Column(name = "del_avg_sma")
    private BigDecimal delAvgSma;

    @Column(name = "foi_avg_sma")
    private BigDecimal foiAvgSma;

}
