package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.core.exception.ValetNotFoundException;
import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositUserForm;
import quickbit.core.util.QuickBitUtil;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;

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
    public Wallet getByUserId(@NotNull Long userId) {
        return walletRepository.findByUserId(userId)
            .orElseThrow(ValetNotFoundException::new);
    }

    @Override
    public Wallet save(@NotNull Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @NotNull
    @Override
    public Wallet getById(@NotNull Long valetId) {
        return walletRepository.findById(valetId)
            .orElseThrow(ValetNotFoundException::new);
    }

    @NotNull
    @Override
    public Wallet getOrCreate(
        @NotNull Long userId,
        @NotNull Long currencyId
    ) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserIdAndCurrencyId(userId, currencyId);

        return optionalWallet.orElseGet(() -> optionalWallet.orElse(
            walletRepository.save(new Wallet())
                .setCurrencyId(currencyId)
                .setUserId(userId)
        ));

    }

    @Override
    public Wallet deposit(
        @NotNull DepositUserForm form,
        @NotNull User user
    ) {
        Wallet wallet = getById(user.getWalletId());
        BigDecimal score = BigDecimal.valueOf(form.getScore());

        Currency formCurrency = currencyService.getByName(form.getCurrencyName());
        Currency waletCurrency = currencyService.getById(wallet.getCurrencyId());

        if (!formCurrency.equals(waletCurrency)) {
            BigDecimal formRate = currencyService.getLastPrice(formCurrency.getId());
            BigDecimal walletRate = currencyService.getLastPrice(waletCurrency.getId());
            score = QuickBitUtil.convert(score, formRate, walletRate);
        }

        score = wallet.getScore().add(score);
        return walletRepository.save(wallet.setScore(score));
    }

    @Override
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

        Wallet wallet = getOrCreate(user.getId(), purchaseCurrency.getId());

        wallet
            .setScore(BigDecimal.valueOf(counter))
            .setCurrency(purchaseCurrency)
            .setUser(user);

        return walletRepository.save(wallet);
    }
}
