package quickbit.core.model.assembler;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import quickbit.core.model.CurrencyModel;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.ImageService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

@Component
public class CurrencyModelAssembler implements RepresentationModelAssembler<Currency, CurrencyModel> {

    private final CurrencyService currencyService;
    private final ImageService imageService;

    @Autowired
    public CurrencyModelAssembler(
        CurrencyService currencyService,
        ImageService imageService
    ) {
        this.currencyService = currencyService;
        this.imageService = imageService;
    }

    @NotNull
    @Override
    public CurrencyModel toModel(@NotNull Currency entity) {
        Set<CurrencyPrice> currencyPriceSet = currencyService.getAllPrices(entity.getId());
        Map<LocalDateTime, Double> pricesMap = new TreeMap<>();

        currencyPriceSet
            .forEach(
                price -> pricesMap.put(price.getCreatedAt(), price.getPrice().doubleValue())
            );

        CurrencyModel model = new CurrencyModel();

        model
            .setFiat(entity.isFiat())
            .setName(entity.getName())
            .setPricesMap(pricesMap);

        if (Objects.nonNull(entity.getAvatar())) {
            model.setAvatar(
                imageService.getById(entity.getAvatar()).getUuid()
            );
        }

        return model;
    }

    public PagedModel<CurrencyModel> toPagedModel(Page<Currency> users) {
        return PagedModel.of(
            users.map(this::toModel).getContent(),
            new PagedModel.PageMetadata(
                users.getSize(),
                users.getNumber(),
                users.getTotalElements()
            )
        );
    }
}
