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
import quickbit.core.model.PriceResponseDataModel;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;
import quickbit.dbcore.entity.CurrencyType;
import quickbit.dbcore.repositories.CurrencyPriceRepository;
import quickbit.util.HttpUtil;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PriceServiceImpl implements PriceService {

    private final OkHttpClient client;
    private final CurrencyPriceRepository currencyPriceRepository;
    private final String apiKey;
    private final String marketUrl = "https://pro-api.coinmarketcap.com";


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
    public void updatePrice(
        @NotNull List<Currency> currencies
    ) {
        PriceResponseDataModel responseModel = sendRequestForUpdate(currencies);

        Set<CurrencyPrice> newCurrencies = new HashSet<>();
        for (PriceResponseDataModel.CurrencyInfo currencyInfo : responseModel.getData().values()) {
            CurrencyType type = CurrencyType.valueOf(currencyInfo.getSymbol());
            newCurrencies.add(
                new CurrencyPrice()
                    .setPrice(
                        currencyInfo
                            .getQuote()
                            .get("2781")
                            .getPrice()
                    )
                    .setCurrency(type)
            );
        }
        currencyPriceRepository.saveAll(newCurrencies);
    }

    private Map<CurrencyType, BigDecimal> processingRequest(
        @NotNull Map<String, Object> response
    ) {
        Map<CurrencyType, BigDecimal> updatedCurrencies = new HashMap<>();
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Map<String, Object> currencyInfo = (Map<String, Object>) entry.getValue();
            Map<String, Object> quote = (Map<String, Object>) currencyInfo.get("quote");
            Map<String, Object> quoteInfo = (Map<String, Object>) quote.get("USD");
            BigDecimal price = (BigDecimal) quoteInfo.get("price");

            CurrencyType currency = CurrencyType.valueOf(currencyInfo.get("symbol").toString());
            updatedCurrencies.put(currency, price);
        }
        return updatedCurrencies;
    }

    private PriceResponseDataModel sendRequestForUpdate(@NotNull List<Currency> currencies) {
        Set<String> currencyNames = currencies
            .stream()
            .map(
                currency -> CurrencyType.getDescription(currency.getName())
            )
            .collect(Collectors.toSet());

        String responseJson = null;
        try {
            URIBuilder query = new URIBuilder(marketUrl + "/v1/cryptocurrency/quotes/latest");
            query.addParameter("slug", String.join(",", currencyNames));
            query.addParameter("convert_id", "2781");

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
        return HttpUtil.parseJsonToModel(responseJson);
    }
}
