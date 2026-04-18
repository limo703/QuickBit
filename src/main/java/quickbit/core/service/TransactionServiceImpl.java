package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.repositories.TransactionRepository;

import java.util.List;

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
    public List<Transaction> findMatchingOrders(
        @NotNull Long currencyId,
        @NotNull Boolean takerTypeOpp,
        @NotNull Double price
    ) {
        if (takerTypeOpp) {
            return transactionRepository.findSellOrdersForBuy(currencyId, price);
        }
        return transactionRepository.findBuyOrdersForSell(currencyId, price);
    }

    @Override
    public int lockTransactionByKey(@NotNull String lockKey) {
        return transactionRepository.lockTransactionByKey(lockKey);
    }

    @Override
    public Page<Transaction> findAllByUserId(
        @NotNull Long userId,
        @NotNull Pageable pageable
    ) {
        return transactionRepository.findAllByUserId(userId, pageable);
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
