package quickbit.core.service;

import quickbit.core.form.CreateTransactionForm;
import quickbit.core.form.DepositUserForm;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import com.sun.istack.NotNull;

public interface WalletService {

    Wallet getByUserId(@NotNull Long userId);

    Wallet save(@NotNull Wallet wallet);

    @NotNull
    Wallet getById(Long valetId);

    @NotNull
    Wallet getOrCreate(
        @NotNull Long userId,
        @NotNull Long currencyId
    );

    Wallet deposit(
        @NotNull DepositUserForm form,
        @NotNull User user
    );

    Wallet processingTransaction(
        @NotNull CreateTransactionForm form,
        @NotNull User user
    );
}
