package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.repositories.TransactionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(
        TransactionRepository transactionRepository
    ) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Set<Transaction> findAllByPurchaseAndSellCurrencies(Long purchaseId, Long sellId) {
        return transactionRepository.findAllByPurchaseCurrencyIdAndSellCurrencyId(purchaseId, sellId);
    }

    @Override
    public Transaction create(
        @NotNull Transaction transaction
    ) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void remove(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public void removeAll(@NotNull Iterable<Transaction> transactions) {
        transactionRepository.deleteAll(transactions);
    }

}
