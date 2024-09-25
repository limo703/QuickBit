package quickbit.core.model.assembler;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import quickbit.core.model.WalletModel;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Wallet;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ValetModelAssembler implements RepresentationModelAssembler<Wallet, WalletModel> {

    private final CurrencyService currencyService;

    @Autowired
    public ValetModelAssembler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @NotNull
    @Override
    public WalletModel toModel(@NotNull Wallet entity) {
        WalletModel walletModel = new WalletModel();

        Currency currency = currencyService.getById(entity.getCurrencyId());

        walletModel
            .setCurrencyName(currency.getName())
            .setScore(entity.getScore().doubleValue());

        return walletModel;
    }
}
