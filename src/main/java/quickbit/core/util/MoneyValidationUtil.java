package quickbit.core.util;

import java.math.BigDecimal;

public final class MoneyValidationUtil {

    private MoneyValidationUtil() {
    }

    public static boolean isPositiveFinite(Double value) {
        if (value == null) {
            return false;
        }
        if (value.isNaN() || value.isInfinite()) {
            return false;
        }
        return Double.compare(value, 0d) > 0;
    }

    public static BigDecimal nonNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
