package quickbit.core.util;


public class CacheConstraints {

    public static final String CURRENCY_PRICE_KEY_VALUE = "#currency.price.key:";

    public static String CURRENCY_PRICE_KEY(Long currencyId) {
        return String.format("#currency.price.key:%s", currencyId.toString());
    }

}
