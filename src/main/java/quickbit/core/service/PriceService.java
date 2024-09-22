package quickbit.core.service;

import com.sun.istack.NotNull;
import quickbit.dbcore.entity.Currency;

import java.util.List;

public interface PriceService {

    void updatePrice(@NotNull List<Currency> currencies);
}
