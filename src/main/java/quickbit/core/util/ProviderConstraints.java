package quickbit.core.util;

public class ProviderConstraints {

    //тут можно цеплять подробную информацию по крипте
    public final static String CURRENCY_PROVIDER_BASE_URL = "https://pro-api.coinmarketcap.com";
    public final static String LATEST_CURRENCY_RATE_URL = "/v1/cryptocurrency/quotes/latest";


    //тут цепляю исключительно актуальный курс для фиатных валют
    public final static String FIAT_CURRENCY_PROVIDER_BASE_URL = "https://api.currencyfreaks.com";
    public final static String FIAT_LATEST_CURRENCY_RATE_URL = "/v2.0/rates/latest";

    //тут беру новости
    public final static String LATEST_NEWS_URL = "https://www.alphavantage.co/query";
}
