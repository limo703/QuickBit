package quickbit.dbcore.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum CurrencyType {
    USD,
    EUR,
    RUB,
    BTC;

    private static final Map<CurrencyType, String> DESCRIPTIONS = new HashMap<>();
    private static final Set<CurrencyType> CRYPTO_CURRENCIES;

    static {
        DESCRIPTIONS.put(BTC, "bitcoin");;
    }

    static {
        CRYPTO_CURRENCIES = Set.of(BTC);
    }

    public static Set<CurrencyType> getCryptoCurrencies() {
        return CRYPTO_CURRENCIES;
    }

    public static String getDescription(CurrencyType currency) {
        return DESCRIPTIONS.get(currency);
    }
}
