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
    private final UserService userService;

    @Autowired
    public WalletServiceImpl(
        WalletRepository walletRepository,
        CurrencyService currencyService,
        TransactionService transactionService,
        UserService userService
    ) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
        this.transactionService = transactionService;
        this.userService = userService;
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

        Set<Transaction> removeTransactions = new HashSet<>();
        Set<Wallet> updatedWallets = new HashSet<>();
        for (Transaction revTransaction : reverseFilteredTransactions) {
            double revTransactionSum = revTransaction.getAmount() * revTransaction.getOperationPrice();
            double revTransactionAmount = revTransaction.getAmount();

            if (revTransactionSum < transactionSum) {
                transactionSum -= revTransactionSum;
                transactionAmount -= revTransactionAmount;
                removeTransactions.add(revTransaction);

                counterSum += revTransactionSum;
                counterAmount += revTransactionAmount;
            } else {
                //сюда должен попасть 1 раз
                revTransactionAmount -= transactionAmount;
                counterSum += transactionSum;
                counterAmount += transactionAmount;

                transactionSum = 0;
                transactionAmount = 0;

                removeTransactions.add(revTransaction);

                if (revTransactionAmount != 0) {
                    transactionService.save(
                        new Transaction()
                            .setUser(revTransaction.getUser())
                            .setOperationPrice(revTransaction.getOperationPrice())
                            .setAmount(revTransactionAmount)
                            .setTypeOpp(revTransaction.getTypeOpp())
                            .setCurrencyId(revTransaction.getCurrencyId())
                    );
                }
                break;
            }
        }

        if (transactionSum != 0 && transactionAmount != 0) {
            transactionService.save(
                new Transaction()
                    .setAmount(transactionAmount)
                    .setOperationPrice(form.getPrice())
                    .setTypeOpp(form.getTypeOpp())
                    .setCurrencyId(oppCurrency.getId())
                    .setUser(user)
            );
        }

        updatedWallets.addAll(
            processingRemoveTransactions(removeTransactions)
        );

        updatedWallets.addAll(
            prepareUserWallets(user, oppCurrency, counterAmount, counterSum, form.getTypeOpp())
        );

        walletRepository.saveAll(updatedWallets);
    }

    private Set<Wallet> processingRemoveTransactions(@NotNull Set<Transaction> removeTransactions) {
        Set<Wallet> updatedWallets = new HashSet<>();

        removeTransactions.forEach(transaction ->
            updatedWallets.addAll(prepareWalletsForCloseTransaction(transaction))
        );

        transactionService.removeAll(removeTransactions);
        return updatedWallets;
    }

    private Set<Wallet> prepareUserWallets(
        @NotNull User user,
        @NotNull Currency currency,
        @NotNull Double counterAmount,
        @NotNull Double counterDefaultAmount,
        boolean typeOpp
    ) {
        Wallet opWallet = getWalletByUserIdAndCurrencyId(user.getId(), currency.getId());
        Wallet defWallet = getDefault(user);

        BigDecimal difAmount = BigDecimal.valueOf(counterAmount);
        BigDecimal difDefAmount = BigDecimal.valueOf(counterDefaultAmount);

        return updateWallets(opWallet, defWallet, difAmount, difDefAmount, typeOpp);
    }

    private Set<Wallet> prepareWalletsForCloseTransaction(@NotNull Transaction transaction) {
        User user = userService.getById(transaction.getUserId());

        Wallet opWallet = getWalletByUserIdAndCurrencyId(transaction.getUserId(), transaction.getCurrencyId());
        Wallet defWallet = getDefault(user);

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
        BigDecimal newOpWalletAmount = opWallet.getAmount();
        BigDecimal newDefWalletAmount = defWallet.getAmount();

        if (typeOpp) {
            newOpWalletAmount = newOpWalletAmount.add(difAmount);
            newDefWalletAmount = newDefWalletAmount.subtract(difDefAmount);
        } else {
            newOpWalletAmount = newOpWalletAmount.subtract(difAmount);
            newDefWalletAmount = newDefWalletAmount.add(difDefAmount);
        }

        return Set.of(
            opWallet.setAmount(newOpWalletAmount),
            defWallet.setAmount(newDefWalletAmount)
        );
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

        BigDecimal newFromAmount = fromWallet.getAmount().subtract(BigDecimal.valueOf(form.getAmount()));

        BigDecimal toDifference = QuickBitUtil.convert(
            BigDecimal.valueOf(form.getAmount()),
            currencyService.getLastPrice(fromCurrency.getId()),
            currencyService.getLastPrice(toCurrency.getId())
        );
        BigDecimal newToAmount = toWallet.getAmount().add(toDifference);

        walletRepository.saveAll(Set.of(
            fromWallet.setAmount(newFromAmount),
            toWallet.setAmount(newToAmount)
        ));
    }

    @Override
    public Wallet getDefault(@NotNull User user) {
        Currency currency = currencyService.getDefault();
        return getWalletByUserIdAndCurrencyId(user.getId(), currency.getId());
    }
}
