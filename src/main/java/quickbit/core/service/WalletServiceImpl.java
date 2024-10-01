package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.core.exception.WalletNotFoundException;
import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositForm;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyService currencyService;
    private final TransactionService transactionService;

    @Autowired
    public WalletServiceImpl(
        WalletRepository walletRepository,
        CurrencyService currencyService,
        TransactionService transactionService
    ) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
        this.transactionService = transactionService;
    }

    @Override
    public Wallet save(@NotNull Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @NotNull
    @Override
    public Wallet getById(@NotNull Long valetId) {
        return walletRepository.findById(valetId)
            .orElseThrow(WalletNotFoundException::new);
    }

    @NotNull
    @Override
    public Wallet getOrCreate(
        @NotNull User user,
        @NotNull Currency currency
    ) {
        Optional<Wallet> optionalWallet =
            walletRepository.findByUserIdAndCurrencyId(user.getId(), currency.getId());

        return optionalWallet.orElseGet(() -> walletRepository.save(
            new Wallet()
                .setCurrency(currency)
                .setUser(user)
                .setAmount(BigDecimal.ZERO)
        ));
    }

    @Override
    @Transactional
    public Wallet deposit(
        @NotNull DepositForm form,
        @NotNull User user
    ) {
        Currency currency = currencyService.getByName(form.getCurrency());
        Wallet wallet = getOrCreate(user, currency);

        BigDecimal addAmount = BigDecimal.valueOf(form.getAmount());
        return walletRepository.save(wallet.add(addAmount));
    }

    @Override
    @Transactional
    public Wallet processingTransaction(
        @NotNull CreateTransactionForm form,
        @NotNull User user
    ) {
        Currency sellCurrency = currencyService.getByName(form.getSellCurrencyName());
        Currency purchaseCurrency = currencyService.getByName(form.getPurchaseCurrencyName());

        Set<Transaction> reverseTransactions = transactionService.findAllByPurchaseAndSellCurrencies(
            sellCurrency.getId(),
            purchaseCurrency.getId()
        );
        reverseTransactions = reverseTransactions
            .stream()
            .filter(transaction -> transaction.getOperationPrice() < form.getPrice())
            .sorted(Comparator.comparing(Transaction::getOperationPrice))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        double operationSum = form.getAmount() * form.getPrice();
        double counter = 0;

        Set<Transaction> removeTransactions = new HashSet<>();
        for (Transaction transaction : reverseTransactions) {
            double revOperationSum = transaction.getAmount() * transaction.getOperationPrice();
            if (revOperationSum < operationSum) {
                operationSum -= revOperationSum;
                counter += revOperationSum;
                removeTransactions.add(transaction);
            } else {
                //сюда должен зайти 1 раз
                double newRevSum = revOperationSum - operationSum;
                counter += operationSum;
                operationSum = 0;
                removeTransactions.add(transaction);
                transactionService.create(
                    new Transaction()
                        .setAmount(newRevSum / transaction.getOperationPrice())
                        .setOperationPrice(transaction.getOperationPrice())
                        .setUserId(transaction.getUserId())
                        .setSellCurrencyId(transaction.getSellCurrencyId())
                );
            }
        }

        transactionService.removeAll(removeTransactions);

        if (operationSum != 0) {
            transactionService.create(
                new Transaction()
                    .setSellCurrencyId(sellCurrency.getId())
                    .setPurchaseCurrencyId(purchaseCurrency.getId())
                    .setAmount(operationSum / form.getPrice())
                    .setOperationPrice(form.getPrice())
                    .setUserId(user.getId())
            );
        }

        Wallet wallet = getOrCreate(user, purchaseCurrency);

        wallet
            .setAmount(BigDecimal.valueOf(counter))
            .setCurrency(purchaseCurrency)
            .setUser(user);

        return walletRepository.save(wallet);
    }

    @Override
    public Set<Wallet> getAllNonDefaultWallets(@NotNull Long userId) {
        return walletRepository.findAllByUserIdWithoutDefault(userId);
    }

    @Override
    public Set<Wallet> findAllFiatWallets(@NotNull Long userId) {
        return walletRepository.findAllFiatByUserId(userId);
    }

    @Override
    public Set<Wallet> findAllNonFiatWallets(@NotNull Long userId) {
        return walletRepository.findAllNotFiatByUserId(userId);
    }

    @Override
    public Optional<Wallet> findWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    ) {
        return walletRepository.findByUserIdAndCurrencyId(userId, currencyId);
    }
}
