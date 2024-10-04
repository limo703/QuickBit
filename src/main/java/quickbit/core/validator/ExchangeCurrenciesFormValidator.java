package quickbit.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import quickbit.core.form.ExchangeCurrenciesForm;
import quickbit.core.model.AuthUser;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.WalletService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Wallet;

import java.util.Optional;

@Component
public class ExchangeCurrenciesFormValidator implements Validator {

    private static final String FROM_CURRENCY_FIELD = "fromCurrency";
    private static final String TO_CURRENCY_FIELD = "toCurrency";
    private static final String AMOUNT_FIELD = "amount";
    private static final String ERROR_INCORRECT_FORMAT = "error.incorrect.format";
    private final static String ERROR_INSUFFICIENT_FUNDS = "error.insufficient.funds";
    private final static String ERROR_CURRENCY_DO_NOT_MATCH = "error.exchange.currency.do.not.match";

    private final CurrencyService currencyService;
    private final WalletService walletService;

    @Autowired
    public ExchangeCurrenciesFormValidator(
        CurrencyService currencyService,
        WalletService walletService
    ) {
        this.currencyService = currencyService;
        this.walletService = walletService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ExchangeCurrenciesForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExchangeCurrenciesForm form = (ExchangeCurrenciesForm) target;

        Optional<Currency> fromCurrency = currencyService.findByName(form.getFromCurrency());
        Optional<Currency> toCurrency = currencyService.findByName(form.getToCurrency());
        
        if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
            errors.rejectValue(FROM_CURRENCY_FIELD, ERROR_INCORRECT_FORMAT);
        }

        if (fromCurrency.get().equals(toCurrency.get())) {
            errors.rejectValue(TO_CURRENCY_FIELD, ERROR_CURRENCY_DO_NOT_MATCH);
        }

        Optional<Wallet> wallet = walletService.findWalletByUserIdAndCurrencyId(
            authUser.getUser().getId(),
            fromCurrency.get().getId()
        );

        if (
            wallet.isEmpty()
            || form.getAmount() == 0
            || wallet.get().getAmount().doubleValue() < form.getAmount()
        ) {
            errors.rejectValue(AMOUNT_FIELD, ERROR_INSUFFICIENT_FUNDS);
        }
    }
}
