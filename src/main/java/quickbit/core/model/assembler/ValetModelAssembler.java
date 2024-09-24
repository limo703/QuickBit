package quickbit.core.model.assembler;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import quickbit.core.model.ValetModel;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Valet;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ValetModelAssembler implements RepresentationModelAssembler<Valet, ValetModel> {

    private final CurrencyService currencyService;

    @Autowired
    public ValetModelAssembler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @NotNull
    @Override
    public ValetModel toModel(@NotNull Valet entity) {
        ValetModel valetModel = new ValetModel();

        Currency currency = currencyService.getById(entity.getCurrencyId());

        valetModel
            .setCurrencyName(currency.getName())
            .setScore(entity.getScore());

        return valetModel;
    }
}
