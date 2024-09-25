package quickbit.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class QuickBitUtil {

    /**
     * Конвертирует валюту
     * @param value колличество валюты
     * @param old старый курс
     * @param update новый курс
     * @return
     */
    public static BigDecimal convert(BigDecimal value, BigDecimal old, BigDecimal update) {
        BigDecimal ratio = old.divide(update, 2, RoundingMode.HALF_UP);
        return value.multiply(ratio);
    }

    public static Double convert(Double value, Double old, Double update) {
        Double ratio = old / update;
        return value * ratio;
    }

}
