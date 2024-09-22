package quickbit.core.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sun.istack.NotNull;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import quickbit.core.model.FiatCurrencyDataModel;
import quickbit.core.model.PriceResponseDataModel;
import quickbit.core.util.HttpUtil;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;
import quickbit.dbcore.entity.CurrencyType;
import quickbit.dbcore.repositories.CurrencyPriceRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static quickbit.core.util.ProviderConstraints.CBR_FIAT_CURRENCY_URL;
import static quickbit.core.util.ProviderConstraints.CURRENCY_PROVIDER_BASE_URL;
import static quickbit.core.util.ProviderConstraints.LATEST_CURRENCY_RATE_URL;

@Service
public class PriceServiceImpl implements PriceService {

    private final OkHttpClient client;
    private final CurrencyPriceRepository currencyPriceRepository;
    private final String apiKey;


    @Autowired
    public PriceServiceImpl(
        CurrencyPriceRepository currencyPriceRepository,
        @Value("${coin.market.cup.api.key}") String apiKey
    ) {
        this.currencyPriceRepository = currencyPriceRepository;
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    @Override
    public void updateFiatCurrency(@NotNull List<Currency> currencies) {
        FiatCurrencyDataModel dataModel = retrieveFiatCurrencyRates();

        List<Currency> currencies;
    }

    @Override
    public void refreshCurrencyPrices(@NotNull List<Currency> currencies) {
        PriceResponseDataModel responseModel = retrieveCurrencyRates(currencies);
        Set<CurrencyPrice> newCurrencies = new HashSet<>();
        for (PriceResponseDataModel.CurrencyInfo currencyInfo : responseModel.getData().values()) {
            CurrencyType type = CurrencyType.valueOf(currencyInfo.getSymbol());
            newCurrencies.add(
                new CurrencyPrice()
                    .setPrice(
                        currencyInfo
                            .getQuote()
                            .get(CurrencyType.USD.getDescription())
                            .getPrice()
                    )
                    .setCurrency(type)
            );
        }
        currencyPriceRepository.saveAll(newCurrencies);
    }

    private PriceResponseDataModel retrieveCurrencyRates(@NotNull List<Currency> currencies) {
        Set<String> currencyNames = currencies
            .stream()
            .map(
                currency -> currency.getName().getDescription()
            )
            .collect(Collectors.toSet());

        String responseJson = null;
        try {
            URIBuilder query = new URIBuilder(CURRENCY_PROVIDER_BASE_URL + LATEST_CURRENCY_RATE_URL);
            query.addParameter("slug", String.join(",", currencyNames));
            query.addParameter("convert_id", CurrencyType.USD.getDescription());

            Request request = new Request.Builder()
                .addHeader(HttpHeaders.ACCEPT, "application/json")
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .url(query.build().toString())
                .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseJson = response.body().string();
            }
        } catch (URISyntaxException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return HttpUtil.parseJsonToModel(responseJson, PriceResponseDataModel.class);
    }

    private FiatCurrencyDataModel retrieveFiatCurrencyRates() {
        String responseJson = null;
        try {
            URI uri = new URIBuilder(CBR_FIAT_CURRENCY_URL).build();

            Request request = new Request.Builder()
                .url(uri.toString())
                .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseJson = response.body().string();
            }
        } catch (URISyntaxException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return HttpUtil.parseJsonToModel(responseJson, FiatCurrencyDataModel.class);
    }
}
