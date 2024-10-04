package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.form.EditUserForm;
import quickbit.core.form.admin.CreateCurrencyForm;
import quickbit.core.form.admin.EditCurrencyForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.UserModel;
import quickbit.core.service.admin.CurrencyAdminService;
import quickbit.core.service.admin.UserAdminService;
import quickbit.core.util.RedirectUtil;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final CurrencyAdminService currencyService;
    private final UserAdminService userService;

    @Autowired
    public AdminController(
        CurrencyAdminService currencyService,
        UserAdminService userService
    ) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @GetMapping("user/all")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView getUsersPage(
        @PageableDefault Pageable pageable,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Page<UserModel> users = userService.findAll(pageable);
        return new ModelAndView("admin/user/all")
            .addObject("users", users);
    }

    @GetMapping("user/edit")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView getUsersPage(
        @RequestParam("uuid") String uuid,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        UserModel user = userService.getByUuid(uuid);
        return new ModelAndView("admin/user/edit")
            .addObject("uuid", user.getUuid())
            .addObject(
                new EditUserForm()
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
            );
    }

    @PostMapping("user/edit")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView editUser(
        @RequestParam("uuid") String uuid,
        @Validated @ModelAttribute("editUserForm")
        EditUserForm editUserForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.edit(editUserForm, uuid);
        return RedirectUtil.redirect("/admin/user/all");
    }

    @GetMapping("currency/all")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView currenciesPage(
        @PageableDefault Pageable pageable,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Page<CurrencyModel> currencies = currencyService.findAll(pageable);
        return new ModelAndView("admin/currency/all")
            .addObject("currencies", currencies);
    }

    @GetMapping("currency/add")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView addCurrencyPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        return new ModelAndView("admin/currency/add")
            .addObject("createCurrencyForm", new CreateCurrencyForm());
    }

    @PostMapping("currency/add")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView addCurrency(
        @Validated @ModelAttribute("createCurrencyForm")
        CreateCurrencyForm createCurrencyForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        currencyService.create(createCurrencyForm);
        return RedirectUtil.redirect("/admin/currency/all");
    }

    @PostMapping("currency/edit")
    @PreAuthorize("@permissionService.isAdmin(#authUser)")
    public ModelAndView editCurrency(
        @Validated @ModelAttribute("editCurrencyForm")
        EditCurrencyForm editCurrencyForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        currencyService.edit(editCurrencyForm);
        return RedirectUtil.redirect("/admin/currency/all");
    }
}
