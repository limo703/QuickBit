package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;

@ControllerAdvice(assignableTypes = CurrencyController.class)
public class CurrencyControllerAdvice {

    public static final String CURRENCY_MODEL = "currencyModel";

    private final CurrencyService currencyService;
    private final CurrencyModelAssembler currencyModelAssembler;

    @Autowired
    public CurrencyControllerAdvice(
        CurrencyService currencyService,
        CurrencyModelAssembler currencyModelAssembler
    ) {
        this.currencyService = currencyService;
        this.currencyModelAssembler = currencyModelAssembler;
    }

    @ModelAttribute
    public void aliasAttribute(
        @PathVariable String currencyName,
        Model model
    ) {
        Currency currency = currencyService.getByName(currencyName);

        model.addAttribute(CURRENCY_MODEL, currencyModelAssembler.toModel(currency));
    }

}
