package org.pra.nse.db.dao.mfi;

import org.pra.nse.db.model.CalcMfiTab;

import java.time.LocalDate;
import java.util.List;

public interface MfiDao {

    int dataCount(LocalDate tradeDate);

    List<CalcMfiTab> getMfi();

    List<CalcMfiTab> getMfi(LocalDate forDate);

    List<CalcMfiTab> getMfi(LocalDate fromDate, LocalDate toDate);

}
