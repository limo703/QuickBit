package quickbit.core.service;

import quickbit.core.exception.ValetNotFoundException;
import quickbit.core.form.DepositUserForm;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;
import quickbit.dbcore.repositories.WalletRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyService currencyService;

    @Autowired
    public WalletServiceImpl(
        WalletRepository walletRepository,
        CurrencyService currencyService
    ) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
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
    public Wallet getById(Long valetId) {
        return walletRepository.findById(valetId)
            .orElseThrow(ValetNotFoundException::new);
    }

    @Override
    public Wallet deposit(
        @NotNull DepositUserForm form,
        @NotNull User user
    ) {
        Wallet wallet = getById(user.getValetId());
        BigDecimal score = BigDecimal.valueOf(form.getScore());

        Currency formCurrency = currencyService.getByName(form.getCurrencyName());
        Currency waletCurrency = currencyService.getById(wallet.getCurrencyId());

        if (!formCurrency.equals(waletCurrency)) {
            BigDecimal formRate = currencyService.getLastPrice(formCurrency.getId());
            BigDecimal waletRate = currencyService.getLastPrice(waletCurrency.getId());

            BigDecimal ratio = formRate.divide(waletRate, 2, RoundingMode.HALF_UP);
            score = score.multiply(ratio);
        }

        score = wallet.getScore().add(score);

        return walletRepository.save(wallet.setScore(score));
    }
}
