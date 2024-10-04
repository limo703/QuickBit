package quickbit.dbcore.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quickbit.dbcore.entity.Transaction;

import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
        value = "SELECT * FROM transaction t " +
            "WHERE t.currency_id = :currencyId " +
            "AND t.type_opp != :typeOpp " +
            "AND ((:typeOpp = true AND t.operation_price <= :price) " +
            "OR (:typeOpp = false AND t.operation_price >= :price)) " +
            "ORDER BY t.operation_price ASC",
        nativeQuery = true
    )
    Set<Transaction> findAllByCurrencyIdAndTypeOppAndPrice(
        @NotNull Long currencyId,
        @NotNull Boolean typeOpp,
        @NotNull Double price
    );

    Page<Transaction> findAllByUserId(@NotNull Long userId, @NotNull Pageable pageable);
}
