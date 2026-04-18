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
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyService currencyService;
    private final OrderMatchingEngine orderMatchingEngine;

    @Autowired
    public WalletServiceImpl(
        WalletRepository walletRepository,
        CurrencyService currencyService,
        OrderMatchingEngine orderMatchingEngine
    ) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
        this.orderMatchingEngine = orderMatchingEngine;
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
        orderMatchingEngine.process(form, user);
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
