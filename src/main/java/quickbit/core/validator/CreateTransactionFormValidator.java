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
import quickbit.core.util.MoneyValidationUtil;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Wallet;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Component
public class CreateTransactionFormValidator implements Validator {

    private final static String PRICE_FIELD = "price";
    private final static String AMOUNT_FIELD = "amount";
    private final static String CURRENCY_NAME_FIELD = "currencyName";
    private final static String TYPE_OPP_FIELD = "typeOpp";
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

        if (Objects.isNull(form.getTypeOpp())) {
            errors.rejectValue(TYPE_OPP_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getPrice())) {
            errors.rejectValue(PRICE_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getAmount())) {
            errors.rejectValue(AMOUNT_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getCurrencyName()) || form.getCurrencyName().isBlank()) {
            errors.rejectValue(CURRENCY_NAME_FIELD, ERROR_IS_EMPTY);
        }

        if (errors.hasErrors()) {
            return;
        }

        if (!MoneyValidationUtil.isPositiveFinite(form.getPrice())) {
            errors.rejectValue(PRICE_FIELD, ERROR_INCORRECT_FORMAT);
        }
        if (!MoneyValidationUtil.isPositiveFinite(form.getAmount())) {
            errors.rejectValue(AMOUNT_FIELD, ERROR_INCORRECT_FORMAT);
        }

        if (errors.hasErrors()) {
            return;
        }

        Optional<Currency> currencyOpt = currencyService.findByName(form.getCurrencyName());
        if (currencyOpt.isEmpty()) {
            errors.rejectValue(CURRENCY_NAME_FIELD, ERROR_INCORRECT_FORMAT);
            return;
        }

        Long userId = authUser.getUser().getId();

        Wallet defWallet = walletService.getDefault(userId);
        Wallet coinWallet = walletService.getWalletByUserIdAndCurrencyId(
            userId,
            currencyOpt.get().getId()
        );

        BigDecimal amount = BigDecimal.valueOf(form.getAmount());
        BigDecimal price = BigDecimal.valueOf(form.getPrice());

        if (form.getTypeOpp()) {
            BigDecimal requiredQuote = amount.multiply(price);
            if (MoneyValidationUtil.nonNull(defWallet.getAmount()).compareTo(requiredQuote) < 0) {
                errors.rejectValue(AMOUNT_FIELD, ERROR_INSUFFICIENT_FUNDS);
            }
        } else {
            if (MoneyValidationUtil.nonNull(coinWallet.getAmount()).compareTo(amount) < 0) {
                errors.rejectValue(AMOUNT_FIELD, ERROR_INSUFFICIENT_FUNDS);
            }
        }
    }
}
