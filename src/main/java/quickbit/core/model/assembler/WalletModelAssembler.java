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
public class WalletModelAssembler implements RepresentationModelAssembler<Wallet, WalletModel> {

    private final CurrencyService currencyService;
    private final SimpleCurrencyModelAssembler simpleCurrencyModelAssembler;

    @Autowired
    public WalletModelAssembler(
        CurrencyService currencyService,
        SimpleCurrencyModelAssembler simpleCurrencyModelAssembler
    ) {
        this.currencyService = currencyService;
        this.simpleCurrencyModelAssembler = simpleCurrencyModelAssembler;
    }

    @NotNull
    @Override
    public WalletModel toModel(@NotNull Wallet entity) {
        WalletModel walletModel = new WalletModel();

        Currency currency = currencyService.getById(entity.getCurrencyId());

        walletModel
            .setAmount(entity.getAmount().doubleValue())
            .setReservedAmount(entity.getReservedAmount().doubleValue())
            .setCurrency(
                simpleCurrencyModelAssembler.toModel(currency)
            );

        return walletModel;
    }
}
