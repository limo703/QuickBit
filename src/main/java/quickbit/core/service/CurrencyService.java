package quickbit.core.service;

import com.sun.istack.NotNull;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    Currency getDefault();

    Currency getByName(@NotNull String currencyName);

    Optional<Currency> findByName(String currencyName);

    List<Currency> findAll();

    List<Currency> findAllFiat();

    List<Currency> findAllFiatWithoutDefault();

    List<Currency> findAllNotFiat();

    Currency getById(long id);

    void saveAllPrice(Iterable<CurrencyPrice> currencies);

    void updateNotFiatCurrency();

    void updateFiatCurrency();
}
