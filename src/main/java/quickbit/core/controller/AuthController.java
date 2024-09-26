package quickbit.core.controller;

import org.springframework.validation.BindingResult;
import quickbit.core.form.CreateUserForm;
import quickbit.core.model.AuthUser;
import quickbit.core.service.UserService;
import quickbit.core.validator.CreateUserFormValidator;
import quickbit.core.util.RedirectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CreateUserFormValidator createUserFormValidator;

    @Autowired
    public AuthController(
        UserService userService,
        CreateUserFormValidator createUserFormValidator
    ) {
        this.userService = userService;
        this.createUserFormValidator = createUserFormValidator;
    }

    @InitBinder("createUserForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(createUserFormValidator);
    }

    @GetMapping("/login")
    public ModelAndView loginPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        if (Objects.isNull(authUser)) {
            RedirectUtil.redirect("/home");
        }

        return new ModelAndView("auth/login");
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        if (Objects.isNull(authUser)) {
            RedirectUtil.redirect("/home");
        }

        return new ModelAndView("auth/registration")
            .addObject("createUserForm", new CreateUserForm());
    }

    @PostMapping("/registration")
    public ModelAndView registration(
        @Validated @ModelAttribute("createUserForm") CreateUserForm createUserForm,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("auth/registration");
        }

        userService.create(createUserForm);

        return RedirectUtil.redirect("/auth/login");
    }
}
