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
    public Set<Transaction> findAllByCurrencyIdAndTypeAndPrice(Long currencyId, Boolean typeOpp, Double price) {
        return transactionRepository.findAllByCurrencyIdAndTypeOppAndPrice(currencyId, typeOpp, price);
    }

    @Override
    public Transaction save(
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
