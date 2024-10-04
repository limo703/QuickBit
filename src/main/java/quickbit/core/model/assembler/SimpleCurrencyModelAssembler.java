package quickbit.core.model.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import quickbit.core.model.SimpleCurrencyModel;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;

import java.math.BigDecimal;

@Component
public class SimpleCurrencyModelAssembler implements RepresentationModelAssembler<Currency, SimpleCurrencyModel> {

    private final CurrencyService currencyService;

    @Autowired
    public SimpleCurrencyModelAssembler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public SimpleCurrencyModel toModel(Currency entity) {
        SimpleCurrencyModel model = new SimpleCurrencyModel();

        BigDecimal lastPrice = currencyService.getLastPrice(entity.getId());

        model
            .setLastPrice(lastPrice.doubleValue())
            .setName(entity.getName());

        return model;
    }

}
