package quickbit.core.validator;

import quickbit.core.form.DepositUserForm;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class DepositUserFormValidator implements Validator {

    private final static String CURRENCY_FILED = "currency";
    private final static String CURRENCY_REQUIRED_ERROR = "currency.required.error";
    private final static String SCORE_FILED = "score";
    private final static String SCORE_WRONG_ERROR = "score.wrong.error";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DepositUserForm.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        DepositUserForm form = (DepositUserForm) target;

        if (Objects.isNull(form.getCurrency())) {
            errors.rejectValue(CURRENCY_FILED, CURRENCY_REQUIRED_ERROR);
        }

        if (Objects.isNull(form.getScore()) || form.getScore() <= 0) {
            errors.rejectValue(SCORE_FILED, SCORE_WRONG_ERROR);
        }
    }
}
