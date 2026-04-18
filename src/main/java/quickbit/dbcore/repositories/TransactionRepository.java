package quickbit.dbcore.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quickbit.dbcore.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
        value = "SELECT * FROM transaction t " +
            "WHERE t.currency_id = :currencyId " +
            "AND t.type_opp = false " +
            "AND t.operation_price <= :price " +
            "ORDER BY t.operation_price ASC, t.id ASC",
        nativeQuery = true
    )
    List<Transaction> findSellOrdersForBuy(
        @NotNull Long currencyId,
        @NotNull Double price
    );

    @Query(
        value = "SELECT * FROM transaction t " +
            "WHERE t.currency_id = :currencyId " +
            "AND t.type_opp = true " +
            "AND t.operation_price >= :price " +
            "ORDER BY t.operation_price DESC, t.id ASC",
        nativeQuery = true
    )
    List<Transaction> findBuyOrdersForSell(
        @NotNull Long currencyId,
        @NotNull Double price
    );

    @Query(
        value = "SELECT count(*) FROM pg_advisory_xact_lock(hashtextextended(:lockKey, 0))",
        nativeQuery = true
    )
    int lockTransactionByKey(@NotNull String lockKey);

    Page<Transaction> findAllByUserId(@NotNull Long userId, @NotNull Pageable pageable);
}
