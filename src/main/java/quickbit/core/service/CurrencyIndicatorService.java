package quickbit.core.service;

import quickbit.core.model.CurrencyIndicatorsModel;
import quickbit.dbcore.entity.Currency;

public interface CurrencyIndicatorService {
    CurrencyIndicatorsModel calculateForCurrency(Currency currency);
}
