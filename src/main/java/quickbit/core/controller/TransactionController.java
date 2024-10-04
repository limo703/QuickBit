package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.form.CreateTransactionForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.model.assembler.TransactionModelAssembler;
import quickbit.core.model.assembler.WalletModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.TransactionService;
import quickbit.core.service.WalletService;
import quickbit.core.util.RedirectUtil;
import quickbit.core.validator.CreateTransactionFormValidator;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;
import quickbit.dbcore.entity.Wallet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Controller
@RequestMapping("transaction")
public class TransactionController {


    private final CurrencyService currencyService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final CurrencyModelAssembler currencyModelAssembler;
    private final WalletModelAssembler walletModelAssembler;
    private final TransactionModelAssembler transactionModelAssembler;
    private final CreateTransactionFormValidator createTransactionFormValidator;

    @Autowired
    public TransactionController(
        CurrencyService currencyService,
        WalletService walletService,
        TransactionService transactionService,
        CurrencyModelAssembler currencyModelAssembler,
        WalletModelAssembler walletModelAssembler,
        TransactionModelAssembler transactionModelAssembler,
        CreateTransactionFormValidator createTransactionFormValidator
    ) {
        this.currencyService = currencyService;
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.currencyModelAssembler = currencyModelAssembler;
        this.walletModelAssembler = walletModelAssembler;
        this.transactionModelAssembler = transactionModelAssembler;
        this.createTransactionFormValidator = createTransactionFormValidator;
    }

    @InitBinder("createTransactionForm")
    public void initCreateTransactionFormBinder(WebDataBinder binder) {
        binder.addValidators(createTransactionFormValidator);
    }

    @GetMapping("create")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView createTransactionPage(
        @RequestParam("currencyName") String currencyName,
        @RequestParam("typeOpp") boolean type,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        User user = authUser.getUser();
        Currency currency = currencyService.getByName(currencyName);
        Currency defaultCurrency = currencyService.getDefault();

        Wallet wallet = walletService.getOrCreate(user, currency);
        Wallet defaultWallet = walletService.getOrCreate(user, defaultCurrency);

        wallet = type ? defaultWallet : wallet;
        BigDecimal price = currencyService.getLastPrice(currency.getId());

        return new ModelAndView("currency/transaction")
            .addObject("currency", currencyModelAssembler.toModel(currency))
            .addObject("typeOpp", type)
            .addObject("lastPrice", price.round( new MathContext(7, RoundingMode.HALF_UP)))
            .addObject("wallet", walletModelAssembler.toModel(wallet))
            .addObject("transactionForm", new CreateTransactionForm());
    }

    @PostMapping("create")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView createTransaction(
        @Validated @ModelAttribute("createTransactionForm")
        CreateTransactionForm createTransactionForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        walletService.processingTransaction(createTransactionForm, authUser.getUser());
        return RedirectUtil.redirect("/home");
    }

    @GetMapping("all")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView transactions(
        UserModel userModel,
        @AuthenticationPrincipal AuthUser authUser,
        @PageableDefault Pageable pageable
    ) {
        Page<Transaction> transactions = transactionService.findAllByUserId(authUser.getUser().getId(), pageable);

        return new ModelAndView("user/transactions")
            .addObject("transactions", transactionModelAssembler.toModels(transactions))
            .addObject("userModel", userModel);
    }
}
