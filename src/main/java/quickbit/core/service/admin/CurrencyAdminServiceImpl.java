package quickbit.core.service.admin;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quickbit.core.form.admin.CreateCurrencyForm;
import quickbit.core.form.admin.EditCurrencyForm;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.assembler.CurrencyModelAssembler;
import quickbit.core.service.CurrencyService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.repositories.CurrencyRepository;

@Service
public class CurrencyAdminServiceImpl implements CurrencyAdminService {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;
    private final CurrencyModelAssembler currencyModelAssembler;

    @Autowired
    public CurrencyAdminServiceImpl(
        CurrencyService currencyService,
        CurrencyRepository currencyRepository,
        CurrencyModelAssembler currencyModelAssembler
    ) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
        this.currencyModelAssembler = currencyModelAssembler;
    }

    @Override
    public Page<CurrencyModel> findAll(@NotNull Pageable pageable) {
        return currencyModelAssembler.toModels(
            currencyService.findAll(pageable)
        );
    }

    @Override
    public CurrencyModel findByName(@NotNull String currencyName) {
        return currencyModelAssembler.toModel(
            currencyService.getByName(currencyName)
        );
    }

    @Override
    public void create(@NotNull CreateCurrencyForm form) {
        Currency currency = new Currency()
            .setFiat(form.isFiat())
            .setName(form.getName())
            .setDescription(form.getDescription());

        currencyRepository.save(currency);
    }

    @Override
    public void edit(
        @NotNull EditCurrencyForm form
    ) {
        Currency currency = currencyService.getByName(form.getCurrencyName());

        currency
            .setFiat(form.isFiat())
            .setName(form.getName())
            .setDescription(form.getDescription());

        currencyRepository.save(currency);
    }
}
