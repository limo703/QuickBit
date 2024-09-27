package quickbit.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import quickbit.core.form.DepositForm;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;

import java.util.Objects;
import java.util.Optional;

@Component
public class DepositFormValidator implements Validator {

    private final static String CURRENCY_FILED = "currency";
    private final static String ERROR_CURRENCY_REQUIRED = "error.currency.required";
    private final static String AMOUNT_FILED = "amount";
    private final static String ERROR_AMOUNT_WRONG = "error.amount.wrong";

    private final CurrencyService currencyService;

    @Autowired
    public DepositFormValidator(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DepositForm.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        DepositForm form = (DepositForm) target;

        Optional<Currency> optionalCurrency = currencyService.findByName(form.getCurrency());
        if (
            Objects.isNull(form.getCurrency())
            || optionalCurrency.isEmpty()
            || !optionalCurrency.get().isFiat()
        ) {
            errors.rejectValue(CURRENCY_FILED, ERROR_CURRENCY_REQUIRED);
        }

        if (Objects.isNull(form.getAmount()) || form.getAmount() <= 0) {
            errors.rejectValue(AMOUNT_FILED, ERROR_AMOUNT_WRONG);
        }
    }
}
