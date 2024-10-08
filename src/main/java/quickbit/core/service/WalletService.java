package quickbit.core.service;

import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositForm;
import quickbit.core.form.ExchangeCurrenciesForm;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import com.sun.istack.NotNull;

import java.util.Optional;
import java.util.Set;

public interface WalletService {

    Wallet save(@NotNull Wallet wallet);

    @NotNull
    Wallet getById(Long walletId);

    Wallet deposit(
        @NotNull DepositForm form,
        @NotNull Long userId
    );

    void processingTransaction(
        @NotNull CreateTransactionForm form,
        @NotNull User user
    );

    Set<Wallet> getAllNonDefaultWallets(@NotNull Long userId);

    Set<Wallet> findAllFiatWallets(@NotNull Long userId);

    Set<Wallet> findAllNonFiatWallets(@NotNull Long userId);

    Wallet getWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    );

    Optional<Wallet> findWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    );

    void exchange(
        @NotNull Long userId,
        @NotNull ExchangeCurrenciesForm form
    );

    Wallet getDefault(@NotNull Long userId);
}
