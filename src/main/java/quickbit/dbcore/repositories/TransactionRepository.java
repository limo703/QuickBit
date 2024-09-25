package quickbit.dbcore.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.repository.CrudRepository;
import quickbit.dbcore.entity.Transaction;

import java.util.Set;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Set<Transaction> findAllByPurchaseCurrencyIdAndSellCurrencyId(
        @NotNull Long purchaseCurrencyId,
        @NotNull Long sellCurrencyId
    );
}
