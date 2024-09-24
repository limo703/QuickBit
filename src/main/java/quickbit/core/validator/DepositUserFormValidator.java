package quickbit.core.validator;

import org.springframework.beans.factory.annotation.Autowired;
import quickbit.core.form.DepositUserForm;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import quickbit.core.service.CurrencyService;

import java.util.Objects;

@Component
public class DepositUserFormValidator implements Validator {

    private final static String CURRENCY_FILED = "currency";
    private final static String ERROR_CURRENCY_REQUIRED = "error.currency.required";
    private final static String SCORE_FILED = "score";
    private final static String ERROR_SCORE_WRONG = "error.score.wrong";

    private final CurrencyService currencyService;

    @Autowired
    public DepositUserFormValidator(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DepositUserForm.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        DepositUserForm form = (DepositUserForm) target;

        if (
            Objects.isNull(form.getCurrencyName())
            || currencyService.findByName(form.getCurrencyName()).isEmpty()
        ) {
            errors.rejectValue(CURRENCY_FILED, ERROR_CURRENCY_REQUIRED);
        }

        if (Objects.isNull(form.getScore()) || form.getScore() <= 0) {
            errors.rejectValue(SCORE_FILED, ERROR_SCORE_WRONG);
        }
    }
}
