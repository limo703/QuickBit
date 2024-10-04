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
import quickbit.core.form.ExchangeCurrenciesForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.model.assembler.WalletModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.WalletService;
import quickbit.core.util.RedirectUtil;
import quickbit.core.validator.DepositFormValidator;
import quickbit.core.validator.ExchangeCurrenciesFormValidator;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Wallet;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("wallet")
public class WalletController {

    private final WalletService walletService;
    private final CurrencyService currencyService;
    private final WalletModelAssembler walletModelAssembler;
    private final CurrencyModelAssembler currencyModelAssembler;
    private final DepositFormValidator depositFormValidator;
    private final ExchangeCurrenciesFormValidator exchangeCurrenciesFormValidator;

    @Autowired
    public WalletController(
        WalletService walletService,
        CurrencyService currencyService,
        WalletModelAssembler walletModelAssembler,
        CurrencyModelAssembler currencyModelAssembler,
        DepositFormValidator depositFormValidator,
        ExchangeCurrenciesFormValidator exchangeCurrenciesFormValidator
    ) {
        this.walletService = walletService;
        this.currencyService = currencyService;
        this.walletModelAssembler = walletModelAssembler;
        this.currencyModelAssembler = currencyModelAssembler;
        this.depositFormValidator = depositFormValidator;
        this.exchangeCurrenciesFormValidator = exchangeCurrenciesFormValidator;
    }

    @InitBinder("depositUserForm")
    public void addValidatorDepositUserForm(WebDataBinder binder) {
        binder.addValidators(depositFormValidator);
    }

    @InitBinder("exchangeUserForm")
    public void addValidatorExchangeUserForm(WebDataBinder binder) {
        binder.addValidators(exchangeCurrenciesFormValidator);
    }

    @GetMapping()
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView depositPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Wallet wallet = walletService.getById(authUser.getUser().getDefaultWalletId());
        Set<Wallet> fiatWallets = walletService.findAllFiatWallets(authUser.getUser().getId());
        List<Currency> fiatCurrencies = currencyService.findAllFiat();

        return new ModelAndView("currency/wallet")
            .addObject("wallet", walletModelAssembler.toModel(wallet))
            .addObject("wallets",
                walletModelAssembler.toCollectionModel(fiatWallets)
            )
            .addObject("currencies",
                currencyModelAssembler.toCollectionModel(fiatCurrencies)
            );
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

    @GetMapping("exchange")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView exchangePage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Set<Wallet> fiatWallets = walletService.findAllFiatWallets(authUser.getUser().getId());
        List<Currency> fiatCurrencies = currencyService.findAllFiat();

        return new ModelAndView("currency/exchange")
            .addObject("wallets",
                walletModelAssembler.toCollectionModel(fiatWallets)
            )
            .addObject("currencies",
                currencyModelAssembler.toCollectionModel(fiatCurrencies)
            );
    }

    @PostMapping("exchange")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView processExchange(
        @Validated @ModelAttribute("exchangeUserForm")
        ExchangeCurrenciesForm exchangeCurrenciesForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        walletService.exchange(authUser.getUser().getId(), exchangeCurrenciesForm);
        return RedirectUtil.redirect("/home");
    }
}
