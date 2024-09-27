package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import quickbit.core.form.DepositForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.assembler.WalletModelAssembler;
import quickbit.core.service.WalletService;
import quickbit.core.util.RedirectUtil;
import quickbit.core.validator.DepositFormValidator;
import quickbit.dbcore.entity.Wallet;

@Controller
@RequestMapping("wallet")
public class WalletController {

    private final WalletService walletService;
    private final WalletModelAssembler assembler;
    private final DepositFormValidator depositFormValidator;

    @Autowired
    public WalletController(
        WalletService walletService,
        WalletModelAssembler walletModelAssembler,
        DepositFormValidator depositFormValidator
    ) {
        this.walletService = walletService;
        this.assembler = walletModelAssembler;
        this.depositFormValidator = depositFormValidator;
    }

    @InitBinder("depositUserForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(depositFormValidator);
    }

    @GetMapping()
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView depositPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Wallet wallet = walletService.getById(authUser.getUser().getDefaultWalletId());
        return new ModelAndView("user/wallet")
            .addObject("wallet", assembler.toModel(wallet));
    }

    @PostMapping("deposit")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView deposit(
        @Validated @ModelAttribute("depositUserForm")
        DepositForm depositForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        walletService.deposit(depositForm, authUser.getUser());
        return RedirectUtil.redirect("/home");
    }
}
