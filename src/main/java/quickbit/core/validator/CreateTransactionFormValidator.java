package quickbit.core.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import quickbit.core.form.CreateTransactionForm;

import java.util.Objects;

@Component
public class CreateTransactionFormValidator implements Validator {

    private final static String PRICE_FIELD = "price";
    private final static String AMOUNT_FIELD = "amount";
    private final static String PURCHASE_CURRENCY_NAME_FIELD = "purchaseCurrencyName";
    private final static String SELL_CURRENCY_NAME_FIELD = "sellCurrencyName";
    private final static String ERROR_IS_EMPTY = "error.is.empty";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateTransactionForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateTransactionForm form = (CreateTransactionForm) target;

        if (Objects.isNull(form.getPrice())) {
            errors.rejectValue(PRICE_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getAmount())) {
            errors.rejectValue(AMOUNT_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getPurchaseCurrencyName())) {
            errors.rejectValue(PURCHASE_CURRENCY_NAME_FIELD, ERROR_IS_EMPTY);
        }
        if (Objects.isNull(form.getSellCurrencyName())) {
            errors.rejectValue(SELL_CURRENCY_NAME_FIELD, ERROR_IS_EMPTY);
        }
    }
}
