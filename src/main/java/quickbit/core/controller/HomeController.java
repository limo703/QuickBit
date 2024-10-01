package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.model.AuthUser;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.NewsModel;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.model.assembler.WalletModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.NewsService;
import quickbit.core.service.WalletService;
import quickbit.dbcore.entity.Wallet;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final CurrencyService currencyService;
    private final WalletService walletService;
    private final NewsService newsService;
    private final UserModelAssembler userModelAssembler;
    private final WalletModelAssembler walletModelAssembler;
    private final CurrencyModelAssembler currencyModelAssembler;

    @Autowired
    public HomeController(
        CurrencyService currencyService,
        WalletService walletService,
        NewsService newsService,
        UserModelAssembler userModelAssembler,
        WalletModelAssembler walletModelAssembler,
        CurrencyModelAssembler currencyModelAssembler
    ) {
        this.newsService = newsService;
        this.currencyService = currencyService;
        this.walletService = walletService;
        this.userModelAssembler = userModelAssembler;
        this.walletModelAssembler = walletModelAssembler;
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

        Set<Wallet> fiatWallets = walletService.findAllFiatWallets(authUser.getUser().getId());
        Set<Wallet> wallets = walletService.findAllNonFiatWallets(authUser.getUser().getId());

        List<NewsModel> newsModels = newsService.getNews();

        return new ModelAndView("home")
            .addObject("authUser", userModelAssembler.toModel(authUser.getUser()))
            .addObject("wallets", walletModelAssembler.toCollectionModel(wallets))
            .addObject("fiatWallets", walletModelAssembler.toCollectionModel(fiatWallets))
            .addObject("currencyModels", criptoCurrencies)
            .addObject("newsModels", newsModels);
    }
}
