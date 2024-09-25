package quickbit.core.service;

import com.sun.istack.NotNull;
import quickbit.dbcore.entity.Transaction;

import java.util.Set;

public interface TransactionService {

    Set<Transaction> findAllByPurchaseAndSellCurrencies(@NotNull Long purchaseId, @NotNull Long sellId);

    Transaction create(@NotNull Transaction transaction);

    void remove(@NotNull Transaction transaction);

    void removeAll(@NotNull Iterable<Transaction> transactions);
}
