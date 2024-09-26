package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.model.AuthUser;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.UserService;
import quickbit.dbcore.entity.Currency;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final UserModelAssembler userModelAssembler;
    private final CurrencyModelAssembler currencyModelAssembler;

    @Autowired
    public HomeController(
        UserService userService,
        CurrencyService currencyService,
        UserModelAssembler userModelAssembler,
        CurrencyModelAssembler currencyModelAssembler
    ) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.userModelAssembler = userModelAssembler;
        this.currencyModelAssembler = currencyModelAssembler;
    }

    @GetMapping()
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView home(
        @AuthenticationPrincipal AuthUser authUser,
        @PageableDefault Pageable pageable
    ) {
        PagedModel<CurrencyModel> criptoCurrencies =
            currencyModelAssembler.toPagedModel(currencyService.findAllNotFiat(pageable));

        return new ModelAndView("home")
            .addObject("authUser", userModelAssembler.toModel(authUser.getUser()))
            .addObject("currencyModels", criptoCurrencies);
    }
}
