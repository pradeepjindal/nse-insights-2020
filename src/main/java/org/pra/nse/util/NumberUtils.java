package org.pra.nse.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static BigDecimal onePercent(BigDecimal figure) {
        return divide(figure, HUNDRED);
    }

    public static BigDecimal divide(BigDecimal to, BigDecimal by) {
        return to.divide(by, 2, RoundingMode.HALF_UP);
    }
}
