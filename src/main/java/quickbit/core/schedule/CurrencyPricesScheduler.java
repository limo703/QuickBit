package quickbit.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quickbit.core.model.data.PriceResponseDataModel;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.NewsService;
import quickbit.dbcore.entity.Currency;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Обновляет цену криптовалюты относительно USD раз в 2 минуты
 * Обновляет цену фиатных валют относительно USD раз в 10 минут
 */
@Component
public class CurrencyPricesScheduler {

    //TODO: Можно вынести времена в проперти
    private final CurrencyService currencyService;
    private final NewsService newsService;

    @Autowired
    public CurrencyPricesScheduler(
        CurrencyService currencyService,
        NewsService newsService
    ) {
        this.currencyService = currencyService;
        this.newsService = newsService;
    }

    @Transactional
    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void scheduledNotFiatCurrency() {
        currencyService.updateNotFiatCurrency();
    }

    @Transactional
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void scheduledFiatCurrency() {
        currencyService.updateFiatCurrency();
    }

//    @Transactional
//    @Scheduled(fixedDelay = 2 * 60 * 1000)
//    public void scheduledNotFiatCurrency() {
//        List<Currency> currencies = currencyService.findAll();
//        PriceResponseDataModel model = newsService.updateNews();
//        model.getData();
//    }
}
