package quickbit.core.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.PriceService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyType;

import java.util.List;

/**
 * Обновляет цену криптовалюты относительно USD раз в 3 минуты
 */
@Component
public class CurrencyPricesScheduler {

    private final CurrencyService currencyService;
    private final PriceService priceService;

    @Autowired
    public CurrencyPricesScheduler(
        CurrencyService currencyService,
        PriceService priceService
    ) {
        this.currencyService = currencyService;
        this.priceService = priceService;
    }

    @Scheduled(fixedDelay = 3 * 60 * 1000)
    public void scheduled() {
        List<Currency> currencies = currencyService.findAll();

        currencies = currencies
            .stream()
            .filter(currency -> CurrencyType.getCryptoCurrencies().contains(currency.getName()))
            .toList();

        priceService.updatePrice(currencies);
    }
}
