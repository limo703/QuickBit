package quickbit.dbcore.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum CurrencyType {
    USD,
    EUR,
    RUB,
    BTC,
    ETH,
    DOGE,
    TON,
    PEPE,
    BNB,
    SOL;

    private static final Map<CurrencyType, String> DESCRIPTIONS = new HashMap<>();
    private static final Set<CurrencyType> CRYPTO_CURRENCIES;
    private static final Set<CurrencyType> FIAT_CURRENCIES;

    static {
        CRYPTO_CURRENCIES = Set.of(BTC, ETH, DOGE, TON, PEPE, SOL, BNB);
        FIAT_CURRENCIES = Set.of(USD, EUR, RUB);

        DESCRIPTIONS.put(USD, "2781");
        DESCRIPTIONS.put(EUR, "2790");
        DESCRIPTIONS.put(RUB, "2806");
        DESCRIPTIONS.put(BTC, "bitcoin");
        DESCRIPTIONS.put(ETH, "ethereum");
        DESCRIPTIONS.put(DOGE, "dogecoin");
        DESCRIPTIONS.put(TON, "toncoin");
        DESCRIPTIONS.put(PEPE, "pepe");
        DESCRIPTIONS.put(BNB, "bnb");
        DESCRIPTIONS.put(SOL, "solana");
    }

    public static Set<CurrencyType> getCryptoCurrencies() {
        return CRYPTO_CURRENCIES;
    }

    public String getDescription() {
        return DESCRIPTIONS.get(this);
    }
}
