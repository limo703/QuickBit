package quickbit.core.service;

import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositForm;
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

    @NotNull
    Wallet getOrCreate(
        @NotNull User user,
        @NotNull Currency currency
    );

    Wallet deposit(
        @NotNull DepositForm form,
        @NotNull User user
    );

    Wallet processingTransaction(
        @NotNull CreateTransactionForm form,
        @NotNull User user
    );

    Set<Wallet> getAllNonDefaultWallets(@NotNull Long userId);

    Set<Wallet> findAllFiatWallets(@NotNull Long userId);

    Set<Wallet> findAllNonFiatWallets(@NotNull Long userId);

    Optional<Wallet> findWalletByUserIdAndCurrencyId(
        @NotNull Long userId,
        @NotNull Long currencyId
    );
}
