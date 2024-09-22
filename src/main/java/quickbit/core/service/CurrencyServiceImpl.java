package quickbit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.repositories.CurrencyPriceRepository;
import quickbit.dbcore.repositories.CurrencyRepository;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyPriceRepository currencyPriceRepository;

    @Autowired
    public CurrencyServiceImpl(
        CurrencyRepository currencyRepository,
        CurrencyPriceRepository currencyPriceRepository
    ) {
        this.currencyRepository = currencyRepository;
        this.currencyPriceRepository = currencyPriceRepository;
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

}
