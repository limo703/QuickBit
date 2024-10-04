package quickbit.core.service.admin;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import quickbit.core.form.admin.CreateCurrencyForm;
import quickbit.core.form.admin.EditCurrencyForm;
import quickbit.core.model.CurrencyModel;

public interface CurrencyAdminService {
    Page<CurrencyModel> findAll(
        @NotNull Pageable pageable
    );

    CurrencyModel findByName(
        @NotNull String currencyName
    );

    void create(@NotNull CreateCurrencyForm createCurrencyForm);

    void edit(@NotNull EditCurrencyForm form);
}
