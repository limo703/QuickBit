package quickbit.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import quickbit.core.form.CreateTransactionForm;
import quickbit.core.model.AuthUser;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.WalletService;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;

import java.util.Objects;

@Component
public class CreateTransactionFormValidator implements Validator {

    private final static String PRICE_FIELD = "price";
    private final static String AMOUNT_FIELD = "amount";
    private final static String CURRENCY_NAME_FIELD = "currencyName";
    private final static String ERROR_IS_EMPTY = "error.is.empty";
    private final static String ERROR_INCORRECT_FORMAT = "error.incorrect.format";
    private final static String ERROR_INSUFFICIENT_FUNDS = "error.insufficient.funds";

    private final WalletService walletService;
    private final CurrencyService currencyService;

    @Autowired
    public CreateTransactionFormValidator(
        WalletService walletService,
        CurrencyService currencyService
    ) {
        this.walletService = walletService;
        this.currencyService = currencyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateTransactionForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateTransactionForm form = (CreateTransactionForm) target;

        if (Objects.isNull(form.getPrice())) {
            errors.rejectValue(PRICE_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getAmount())) {
            errors.rejectValue(AMOUNT_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getCurrencyName())) {
            errors.rejectValue(CURRENCY_NAME_FIELD, ERROR_IS_EMPTY);
        }

        User user = authUser.getUser();

        Wallet defWallet = walletService.getDefault(user);
        Wallet coinWallet = walletService.getOrCreate(user, currencyService.getByName(form.getCurrencyName()));

        if (form.getTypeOpp()) {
            if (defWallet.getAmount().doubleValue() < form.getAmount() * form.getPrice()) {
                errors.rejectValue(AMOUNT_FIELD, ERROR_INSUFFICIENT_FUNDS);
            }
        } else {
            if (coinWallet.getAmount().doubleValue() < form.getAmount()) {
                errors.rejectValue(AMOUNT_FIELD, ERROR_INSUFFICIENT_FUNDS);
            }
        }

    }
}
