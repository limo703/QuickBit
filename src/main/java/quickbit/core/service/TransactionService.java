package quickbit.core.service;

import com.sun.istack.NotNull;
import quickbit.dbcore.entity.Transaction;

import java.util.Set;

public interface TransactionService {

    Set<Transaction> findAllByCurrencyIdAndTypeAndPrice(
        @NotNull Long currencyId,
        @NotNull Boolean typeOpp,
        @NotNull Double price
    );

    Transaction save(@NotNull Transaction transaction);

    void remove(@NotNull Transaction transaction);

    void removeAll(@NotNull Iterable<Transaction> transactions);
}
