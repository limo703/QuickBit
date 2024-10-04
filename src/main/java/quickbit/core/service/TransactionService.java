package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import quickbit.dbcore.entity.Transaction;

import java.util.Set;

public interface TransactionService {

    Set<Transaction> findAllByCurrencyIdAndTypeAndPrice(
        @NotNull Long currencyId,
        @NotNull Boolean typeOpp,
        @NotNull Double price
    );

    Page<Transaction> findAllByUserId(@NotNull Long userId, @NotNull Pageable pageable);

    Transaction save(@NotNull Transaction transaction);

    void remove(@NotNull Transaction transaction);

    void removeAll(@NotNull Iterable<Transaction> transactions);
}
