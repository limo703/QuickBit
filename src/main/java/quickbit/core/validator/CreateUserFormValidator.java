package quickbit.core.validator;

import com.sun.istack.NotNull;
import org.apache.commons.lang3.StringUtils;
import quickbit.core.form.CreateUserForm;
import quickbit.core.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class CreateUserFormValidator implements Validator {

    private final static String USERNAME_FIELD = "username";
    private final static String PASSWORD_FIELD = "password";
    private final static String EMAIL_FIELD = "email";
    private final static String CONFIRM_PASSWORD_FIELD = "confirmPassword";
    private final static String ERROR_PASSWORD_DO_NOT_MATCH = "error.password.do.not.match";
    private final static String ERROR_USERNAME_ALREADY_USE = "error.username.already.use";
    private final static String ERROR_USERNAME_LENGTH = "error.username.length";
    private final static String ERROR_EMAIL_ALREADY_USE = "error.email.already.use";
    private final static String ERROR_IS_EMPTY = "error.is.empty";

    private final UserService userService;

    @Autowired
    public CreateUserFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CreateUserForm.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        CreateUserForm form = (CreateUserForm) target;

        if (StringUtils.isBlank(form.getUsername())) {
            errors.rejectValue(USERNAME_FIELD, ERROR_IS_EMPTY);
        }

        if (StringUtils.isBlank(form.getPassword())) {
            errors.rejectValue(PASSWORD_FIELD, ERROR_IS_EMPTY);
        }

        if (StringUtils.isBlank(form.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, ERROR_IS_EMPTY);
        }

        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD, ERROR_PASSWORD_DO_NOT_MATCH);
        }

        if (Objects.nonNull(userService.getByUsername(form.getUsername()))) {
            errors.rejectValue(USERNAME_FIELD, ERROR_USERNAME_ALREADY_USE);
        }

        if (form.getUsername().length() > 35) {
            errors.rejectValue(USERNAME_FIELD, ERROR_USERNAME_LENGTH);
        }

        if (!EmailValidator.getInstance().isValid(form.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, ERROR_IS_EMPTY);
        }

        if (Objects.nonNull(userService.getByEmail(form.getEmail()))) {
            errors.rejectValue(EMAIL_FIELD, ERROR_EMAIL_ALREADY_USE);
        }
    }
}
