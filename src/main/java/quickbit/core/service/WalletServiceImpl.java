package quickbit.core.service;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickbit.core.exception.WalletNotFoundException;
import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositForm;
import quickbit.core.form.ExchangeCurrenciesForm;
import quickbit.core.util.QuickBitUtil;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Override
    @Transactional
    public Wallet deposit(
        @NotNull DepositForm form,
        @NotNull Long userId
    ) {
        Currency currency = currencyService.getByName(form.getCurrency());
        Wallet wallet = getWalletByUserIdAndCurrencyId(userId, currency.getId());

        BigDecimal addAmount = BigDecimal.valueOf(form.getAmount());
        return walletRepository.save(wallet.add(addAmount));
    }

    @Override
    @Transactional
    public void processingTransaction(
        @NotNull CreateTransactionForm form,
        @NotNull User user
    ) {
        Currency oppCurrency = currencyService.getByName(form.getCurrencyName());

        Set<Transaction> reverseFilteredTransactions = transactionService.findAllByCurrencyIdAndTypeAndPrice(
            oppCurrency.getId(), form.getTypeOpp(), form.getPrice()
        );

        double transactionSum = form.getAmount() * form.getPrice();
        double transactionAmount = form.getAmount();

        double counterSum = 0;
        double counterAmount = 0;

        Set<Transaction> closedTransactions = new HashSet<>();
        Set<Wallet> updatedWallets = new HashSet<>();
        for (Transaction revTransaction : reverseFilteredTransactions) {
            double revTransactionSum = revTransaction.getAmount() * revTransaction.getOperationPrice();
            double revTransactionAmount = revTransaction.getAmount();

            if (revTransactionSum < transactionSum) {
                transactionSum -= revTransactionSum;
                transactionAmount -= revTransactionAmount;
                closedTransactions.add(revTransaction);

                counterSum += revTransactionSum;
                counterAmount += revTransactionAmount;
            } else {
                //сюда должен попасть 1 раз
                revTransactionAmount -= transactionAmount;
                counterSum += transactionSum;
                counterAmount += transactionAmount;

                transactionSum = 0;
                transactionAmount = 0;

                closedTransactions.add(revTransaction);

                if (revTransactionAmount != 0) {
                    processingCreateTransaction(
                        revTransactionAmount,
                        revTransaction.getOperationPrice(),
                        revTransaction.getTypeOpp(),
                        revTransaction.getCurrencyId(),
                        revTransaction.getUser()
                    );
                }
                break;
            }
        }

        if (transactionSum != 0 && transactionAmount != 0) {
            processingCreateTransaction(
                transactionAmount, form.getPrice(), form.getTypeOpp(), oppCurrency.getId(), user
            );
        }

        updatedWallets.addAll(
            processingClosedTransactions(closedTransactions)
        );

        updatedWallets.addAll(
            prepareUserWallets(user.getId(), oppCurrency, counterAmount, counterSum, form.getTypeOpp())
        );

        walletRepository.saveAll(updatedWallets);
    }

    private void processingCreateTransaction(
        @NotNull Double amount,
        @NotNull Double price,
        @NotNull Boolean typeOpp,
        @NotNull Long currencyId,
        @NotNull User user
    ) {
        Wallet wallet;
        BigDecimal reservedAmount;
        if (typeOpp) {
            wallet = getDefault(user.getId());
            reservedAmount = BigDecimal.valueOf(amount * price);
        } else {
            wallet = getWalletByUserIdAndCurrencyId(user.getId(), currencyId);
            reservedAmount = BigDecimal.valueOf(amount);
        }
        wallet.addReserved(reservedAmount);
        wallet.subtract(reservedAmount);
        walletRepository.save(wallet);

        transactionService.save(
            new Transaction()
                .setAmount(amount)
                .setOperationPrice(price)
                .setTypeOpp(typeOpp)
                .setCurrencyId(currencyId)
                .setUser(user)
        );
    }

    private Set<Wallet> processingClosedTransactions(@NotNull Set<Transaction> closedTransactions) {
        Set<Wallet> updatedWallets = new HashSet<>();

        closedTransactions.forEach(transaction ->
            updatedWallets.addAll(prepareWalletsForCloseTransaction(transaction))
        );

        transactionService.removeAll(closedTransactions);
        return updatedWallets;
    }

    private Set<Wallet> prepareUserWallets(
        @NotNull Long userId,
        @NotNull Currency currency,
        @NotNull Double counterAmount,
        @NotNull Double counterDefaultAmount,
        boolean typeOpp
    ) {
        Wallet opWallet = getWalletByUserIdAndCurrencyId(userId, currency.getId());
        Wallet defWallet = getDefault(userId);

        BigDecimal difAmount = BigDecimal.valueOf(counterAmount);
        BigDecimal difDefAmount = BigDecimal.valueOf(counterDefaultAmount);

        return updateWallets(opWallet, defWallet, difAmount, difDefAmount, typeOpp);
    }

    private Set<Wallet> prepareWalletsForCloseTransaction(@NotNull Transaction transaction) {
        Wallet opWallet = getWalletByUserIdAndCurrencyId(transaction.getUserId(), transaction.getCurrencyId());
        Wallet defWallet = getDefault(transaction.getUserId());

        BigDecimal difAmount = BigDecimal.valueOf(transaction.getAmount());
        BigDecimal difDefAmount = BigDecimal.valueOf(transaction.getAmount() * transaction.getOperationPrice());

        return updateWallets(opWallet, defWallet, difAmount, difDefAmount, transaction.getTypeOpp());
    }

    private Set<Wallet> updateWallets(
        Wallet opWallet,
        Wallet defWallet,
        BigDecimal difAmount,
        BigDecimal difDefAmount,
        boolean typeOpp
    ) {
        if (typeOpp) {
            opWallet.add(difAmount);
            defWallet.subtractReserved(difDefAmount);
        } else {
            opWallet.add(difDefAmount);
            defWallet.subtractReserved(difAmount);
        }

        return Set.of(opWallet, defWallet);
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
    public Wallet getWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    ) {
        return walletRepository.findByUserIdAndCurrencyId(userId, currencyId)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<Wallet> findWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    ) {
        return walletRepository.findByUserIdAndCurrencyId(userId, currencyId);
    }

    @Override
    public void exchange(
        @NotNull Long userId,
        @NotNull ExchangeCurrenciesForm form
    ) {
        Currency fromCurrency = currencyService.getByName(form.getFromCurrency());
        Currency toCurrency = currencyService.getByName(form.getToCurrency());

        Wallet fromWallet = getWalletByUserIdAndCurrencyId(userId, fromCurrency.getId());
        Wallet toWallet = getWalletByUserIdAndCurrencyId(userId, toCurrency.getId());

        fromWallet.subtract(BigDecimal.valueOf(form.getAmount()));

        BigDecimal toDifference = QuickBitUtil.convert(
            BigDecimal.valueOf(form.getAmount()),
            currencyService.getLastPrice(fromCurrency.getId()),
            currencyService.getLastPrice(toCurrency.getId())
        );
        toWallet.add(toDifference);

        walletRepository.saveAll(Set.of(
            fromWallet,
            toWallet
        ));
    }

    @Override
    public Wallet getDefault(@NotNull Long userId) {
        Currency currency = currencyService.getDefault();
        return getWalletByUserIdAndCurrencyId(userId, currency.getId());
    }
}
