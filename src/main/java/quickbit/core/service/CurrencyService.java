package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CurrencyService {

    Currency getDefault();

    Currency getByName(@NotNull String currencyName);

    Optional<Currency> findByName(@NotNull String currencyName);

    List<Currency> findAll();

    Page<Currency> findAll(@NotNull Pageable pageable);


    List<Currency> findAllFiat();

    List<Currency> findAllFiatWithoutDefault();

    List<Currency> findAllNotFiat();
    Page<Currency> findAllNotFiat(@NotNull Pageable pageable);

    Currency getById(long id);

    void saveAllPrice(@NotNull Iterable<CurrencyPrice> currencies);

    BigDecimal getLastPrice(@NotNull Long currencyId);

    @NotNull
    Set<CurrencyPrice> getAllPrices(@NotNull Long currencyId);

    void updateNotFiatCurrency();

    void updateFiatCurrency();
}
