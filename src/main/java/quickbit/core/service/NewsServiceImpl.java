package quickbit.core.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import quickbit.core.model.data.PriceResponseDataModel;
import quickbit.core.service.cache.NewsCacheService;
import quickbit.core.util.HttpUtil;
import quickbit.dbcore.entity.Currency;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static quickbit.core.util.ProviderConstraints.LATEST_NEWS_URL;

@Service
public class NewsServiceImpl implements NewsService {

    private final OkHttpClient client;
    private final CurrencyService currencyService;
    private final NewsCacheService newsCacheService;
    private final String apiKey;

    @Autowired
    public NewsServiceImpl(
        CurrencyService currencyService, NewsCacheService newsCacheService,
        @Value("${alpha.vantage.api.key}") String apiKey
    ) {
        this.currencyService = currencyService;
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.newsCacheService = newsCacheService;
    }


    @Override
    public PriceResponseDataModel updateNews() {
        String formattedDate =
            LocalDateTime
                .now()
                .minusMinutes(30)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        String responseJson = null;
        try {
            URIBuilder query = new URIBuilder(LATEST_NEWS_URL);
            query.addParameter("function", "NEWS_SENTIMENT");
            query.addParameter("tickers", "COIN,CRYPTO:BTC,FOREX:USD");
            query.addParameter("limit", "500");
            query.addParameter("time_from", formattedDate);
            query.addParameter("apikey", apiKey);

            Request request = new Request.Builder()
                .addHeader(HttpHeaders.ACCEPT, "application/json")
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
}
