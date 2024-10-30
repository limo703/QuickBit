package quickbit.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quickbit.core.service.CurrencyService;

/**
 * Обновляет цену криптовалюты относительно USD раз в 2 минуты
 * Обновляет цену фиатных валют относительно USD раз в 10 минут
 */
@Component
public class CurrencyPricesScheduler {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyPricesScheduler(
        CurrencyService currencyService
    ) {
        this.currencyService = currencyService;
    }

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void scheduledNotFiatCurrency() {
        currencyService.updateNotFiatCurrency();
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void scheduledFiatCurrency() {
        currencyService.updateFiatCurrency();
    }
}
