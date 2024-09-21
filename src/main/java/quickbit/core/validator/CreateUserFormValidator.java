package quickbit.core.validator;

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
    private final static String REPEATED_PASSWORD_FIELD = "repeatedPassword";

    private final UserService userService;

    @Autowired
    public CreateUserFormValidator(
        UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CreateUserForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUserForm form = (CreateUserForm) target;

        if (Objects.isNull(form.getUsername())) {
            errors.rejectValue(USERNAME_FIELD,  "Username is required");
        }

        if (Objects.isNull(form.getPassword())) {
            errors.rejectValue(PASSWORD_FIELD, "Password is required");
        }

        if (Objects.isNull(form.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, "Email is required");
        }

        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            errors.rejectValue(REPEATED_PASSWORD_FIELD, "Passwords do not match");
        }

        if (Objects.nonNull(userService.getByUsername(form.getUsername()))) {
            errors.rejectValue(USERNAME_FIELD, "Username is already in use");
        }

        if (form.getUsername().length() > 35) {
            errors.rejectValue(USERNAME_FIELD, "Username is too long (max = 35 characters)");
        }

        if (!EmailValidator.getInstance().isValid(form.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, "Invalid email");
        }

        if (Objects.nonNull(userService.getByEmail(form.getEmail()))) {
            errors.rejectValue(USERNAME_FIELD, "Username is already in use");
        }
    }
}
