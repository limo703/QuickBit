package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import quickbit.dbcore.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> findMatchingOrders(
        @NotNull Long currencyId,
        @NotNull Boolean takerTypeOpp,
        @NotNull Double price
    );

    int lockTransactionByKey(@NotNull String lockKey);

    Page<Transaction> findAllByUserId(@NotNull Long userId, @NotNull Pageable pageable);

    Transaction save(@NotNull Transaction transaction);

    void remove(@NotNull Transaction transaction);

    void removeAll(@NotNull Iterable<Transaction> transactions);
}
