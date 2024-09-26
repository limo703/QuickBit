package quickbit.core.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickbit.core.model.AuthUser;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;

@RestController
@RequestMapping("api/currency")
public class CurrencyApiController {

    private final CurrencyService currencyService;
    private final CurrencyModelAssembler currencyModelAssembler;

    @Autowired
    public CurrencyApiController(
        CurrencyService currencyService,
        CurrencyModelAssembler currencyModelAssembler
    ) {
        this.currencyService = currencyService;
        this.currencyModelAssembler = currencyModelAssembler;
    }

    @GetMapping("all")
    @PreAuthorize("@permissionService.check(#authUser)")
    public PagedModel<CurrencyModel> home(
        @AuthenticationPrincipal AuthUser authUser,
        @PageableDefault Pageable pageable
    ) {
        Page<Currency> currencies = currencyService.findAllNotFiat(pageable);

        return currencyModelAssembler.toPagedModel(currencies);
    }
}
