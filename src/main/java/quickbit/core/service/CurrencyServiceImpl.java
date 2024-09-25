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
import quickbit.core.service.cache.CurrencyPriceCacheService;
import quickbit.core.util.HttpUtil;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;
import quickbit.dbcore.repositories.CurrencyPriceRepository;
import quickbit.dbcore.repositories.CurrencyRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static quickbit.core.util.ProviderConstraints.CURRENCY_PROVIDER_BASE_URL;
import static quickbit.core.util.ProviderConstraints.FIAT_CURRENCY_PROVIDER_BASE_URL;
import static quickbit.core.util.ProviderConstraints.FIAT_LATEST_CURRENCY_RATE_URL;
import static quickbit.core.util.ProviderConstraints.LATEST_CURRENCY_RATE_URL;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyPriceRepository currencyPriceRepository;
    private final CurrencyPriceCacheService currencyPriceCacheService;
    private final String apiKey;
    private final String fiatApiKey;
    private final OkHttpClient client;

    @Autowired
    public CurrencyServiceImpl(
        CurrencyRepository currencyRepository,
        CurrencyPriceRepository currencyPriceRepository,
        CurrencyPriceCacheService currencyPriceCacheService,
        @Value("${coin.market.cup.api.key}") String apiKey,
        @Value("${currency.freaks.api.key}") String fiatApiKey
    ) {
        this.currencyRepository = currencyRepository;
        this.currencyPriceRepository = currencyPriceRepository;
        this.currencyPriceCacheService = currencyPriceCacheService;
        this.client = new OkHttpClient();
        this.apiKey = apiKey;
        this.fiatApiKey = fiatApiKey;
    }

    //нужно сделать настраиваемым этот момент и дописать логику
    @Override
    public Currency getDefault() {
        return currencyRepository.findByName("USD")
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Currency getByName(String currencyName) {
        return currencyRepository.findByName(currencyName)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<Currency> findByName(String currencyName) {
        return currencyRepository.findByName(currencyName);
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public List<Currency> findAllFiat() {
        return currencyRepository.findAllByIsFiatIsTrue();
    }

    @Override
    public List<Currency> findAllFiatWithoutDefault() {
        List<Currency> currencies = currencyRepository.findAllByIsFiatIsTrue();
        currencies.remove(getDefault());
        return currencies;
    }

    @Override
    public List<Currency> findAllNotFiat() {
        return currencyRepository.findAllByIsFiatIsFalse();
    }

    @Override
    public Currency getById(long id) {
        return currencyRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void saveAllPrice(Iterable<CurrencyPrice> prices) {
        currencyPriceRepository.saveAll(prices);
        prices.forEach(
            price -> currencyPriceCacheService.save(price.getCurrencyId(), price.getPrice())
        );
    }

    @Override
    public BigDecimal getLastPrice(Long currencyId) {
        BigDecimal bigDecimal = currencyPriceCacheService.get(currencyId);
        if (Objects.nonNull(bigDecimal)) {
            return bigDecimal;
        }
        CurrencyPrice price = currencyPriceRepository.findTopByCurrencyId(currencyId)
            .orElseThrow(EntityNotFoundException::new);

        return price.getPrice();
    }

    @Override
    public void updateNotFiatCurrency() {
        List<Currency> currencies = findAllNotFiat();
        Currency defaultCurrency = getDefault();
        PriceResponseDataModel responseModel =
            retrieveNotFiatCurrencyRates(currencies, defaultCurrency.getDescription());

        Set<CurrencyPrice> newCurrencies = new HashSet<>();
        for (PriceResponseDataModel.CurrencyInfo currencyInfo : responseModel.getData().values()) {
            String currencyName = currencyInfo.getSymbol();
            Currency currency = currencies
                .stream()
                .filter(cur -> currencyName.equals(cur.getName()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

            newCurrencies.add(
                new CurrencyPrice()
                    .setPrice(
                        currencyInfo
                            .getQuote()
                            .get("2781")
                            .getPrice()
                    )
                    .setCurrency(currency)
                    .setCurrencyId(currency.getId())
            );
        }
        saveAllPrice(newCurrencies);
    }

    private PriceResponseDataModel retrieveNotFiatCurrencyRates(
        @NotNull List<Currency> currencies,
        @NotNull String defaultCurrencyCode
    ) {
        Set<String> currencyNames = currencies
            .stream()
            .map(Currency::getDescription)
            .collect(Collectors.toSet());

        String responseJson = null;
        try {
            URIBuilder query = new URIBuilder(CURRENCY_PROVIDER_BASE_URL + LATEST_CURRENCY_RATE_URL);
            query.addParameter("slug", String.join(",", currencyNames));
            query.addParameter("convert_id", defaultCurrencyCode);

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

    @Override
    public void updateFiatCurrency() {
        List<Currency> currencies = findAllFiatWithoutDefault();
        FiatCurrencyDataModel dataModel = retrieveFiatCurrencyRates();

        Set<CurrencyPrice> newPrices = new HashSet<>();
        for (Currency currency : currencies) {
            Double price = dataModel.getRates().get(currency.getName());

            newPrices.add(
                new CurrencyPrice()
                    .setPrice(BigDecimal.valueOf(1/price))
                    .setCurrency(currency)
                    .setCurrencyId(currency.getId())
            );
        }
        saveAllPrice(newPrices);
    }

    private FiatCurrencyDataModel retrieveFiatCurrencyRates() {
        String responseJson = null;
        try {
            URIBuilder query = new URIBuilder(FIAT_CURRENCY_PROVIDER_BASE_URL + FIAT_LATEST_CURRENCY_RATE_URL);
            query.addParameter("apikey", fiatApiKey);

            Request request = new Request.Builder()
                .url(query.build().toString())
                .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseJson = response.body().string();
            }

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return HttpUtil.parseJsonToModel(responseJson, FiatCurrencyDataModel.class);
    }
}
