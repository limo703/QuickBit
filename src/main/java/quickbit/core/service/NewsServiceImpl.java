package quickbit.core.service;

import com.squareup.okhttp.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quickbit.core.exception.CurrencyRequestException;
import quickbit.core.model.NewsModel;
import quickbit.core.model.data.NewsDataModel;
import quickbit.core.service.cache.NewsCacheService;
import quickbit.core.util.HttpUtil;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static quickbit.core.util.ProviderConstraints.LATEST_NEWS_URL;

@Service
public class NewsServiceImpl implements NewsService {
    private static final Logger LOGGER = Logger.getLogger(NewsServiceImpl.class.getName());

    private final NewsCacheService cacheService;
    private final String apiKey;

    @Autowired
    public NewsServiceImpl(
        NewsCacheService newsCacheService,
        @Value("${alpha.vantage.api.key}") String apiKey
    ) {
        this.apiKey = apiKey;
        this.cacheService = newsCacheService;
    }

    @Override
    public List<NewsModel> getNews() {
        return cacheService.getNews()
            .stream()
            .sorted(Comparator.comparing(NewsModel::getCreatedAt).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public void updateNews() {
        NewsDataModel dataModel = retrieveNews();
        List<NewsModel> updatedNews = new LinkedList<>();
        for (NewsDataModel.FeedItem feed : dataModel.getFeed()) {
            LocalDateTime createdAt = LocalDateTime.parse(
                feed.getTime_published(),
                DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
            );

            NewsModel model = new NewsModel(
                feed.getTitle(),
                feed.getSummary(),
                feed.getUrl(),
                createdAt
            );
            updatedNews.add(model);
        }
        cacheService.clearNewsCache();
        cacheService.saveNews(updatedNews);
    }

    private NewsDataModel retrieveNews() {
        String formattedDate =
            LocalDateTime
                .now()
                .minusDays(15)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        try {
            URIBuilder query = new URIBuilder(LATEST_NEWS_URL);
            query.addParameter("function", "NEWS_SENTIMENT");
            query.addParameter("tickers", "COIN");
            query.addParameter("time_from", formattedDate);
            query.addParameter("limit", "50");
            query.addParameter("apikey", apiKey);

            Request request = new Request.Builder()
                .url(query.build().toString())
                .build();

            return HttpUtil.sendRequest(request, NewsDataModel.class);

        } catch (URISyntaxException e) {
            LOGGER.warning("При запросе новостей произошла ошибка");
            throw new CurrencyRequestException(e.getMessage());
        }
    }
}
