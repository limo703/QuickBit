package quickbit.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickbit.core.service.NewsService;


/**
 * Обновляет новости раз в 5 часов
 */
@Component
public class NewsScheduler {

    private final NewsService newsService;

    @Autowired
    public NewsScheduler(NewsService newsService) {
        this.newsService = newsService;
    }

    @Scheduled(fixedDelay = 5 * 60 * 60 * 1000)
    public void scheduledNotFiatCurrency() {
        newsService.updateNews();
    }
}
