package quickbit.core.model.assembler;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import quickbit.core.model.CurrencyModel;
import quickbit.core.model.TransactionModel;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.UserService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.Transaction;
import quickbit.dbcore.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionModelAssembler implements RepresentationModelAssembler<Transaction, TransactionModel> {

    private final UserService userService;
    private final CurrencyService currencyService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public TransactionModelAssembler(
        UserService userService,
        CurrencyService currencyService,
        UserModelAssembler userModelAssembler
    ) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.userModelAssembler = userModelAssembler;
    }

    @NotNull
    @Override
    public TransactionModel toModel(@NotNull Transaction entity) {
        TransactionModel model = new TransactionModel();

        User user = userService.getById(entity.getUserId());
        Currency currency = currencyService.getById(entity.getCurrencyId());

        model
            .setAmount(entity.getAmount())
            .setCurrencyName(currency.getName())
            .setOperationPrice(entity.getOperationPrice())
            .setTypeOpp(entity.getTypeOpp())
            .setUser(
                userModelAssembler.toModel(user)
            );

        return model;
    }

    public Page<TransactionModel> toModels(Page<? extends Transaction> entities) {
        List<TransactionModel> transactionModels = entities.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return new PageImpl<>(transactionModels, entities.getPageable(), entities.getTotalElements());
    }
}
