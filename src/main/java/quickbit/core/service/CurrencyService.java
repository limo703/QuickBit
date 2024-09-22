package quickbit.core.service;

import quickbit.dbcore.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> findAll();
}
