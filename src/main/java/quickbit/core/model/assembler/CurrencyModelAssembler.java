package quickbit.core.model.assembler;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import quickbit.core.model.CurrencyModel;
import quickbit.core.service.CurrencyService;
import quickbit.core.service.ImageService;
import quickbit.dbcore.entity.Currency;
import quickbit.dbcore.entity.CurrencyPrice;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
        Map<String, Double> pricesMap = new TreeMap<>();
        Map<String, Double> volumesMap = new TreeMap<>();

        currencyPriceSet
            .forEach(
                price -> {
                    String key = price.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    pricesMap.put(key, price.getPrice().doubleValue());
                    volumesMap.put(
                        key,
                        Objects.nonNull(price.getVolume24h()) ? price.getVolume24h().doubleValue() : 0D
                    );
                }
            );

        CurrencyModel model = new CurrencyModel();

        model
            .setFiat(entity.isFiat())
            .setName(entity.getName())
            .setPricesMap(pricesMap)
            .setVolumesMap(volumesMap);

        if (Objects.nonNull(entity.getAvatar())) {
            model.setAvatar(
                imageService.getById(entity.getAvatar()).getUuid()
            );
        }

        return model;
    }

    public Page<CurrencyModel> toModels(Page<? extends Currency> entities) {
        List<CurrencyModel> currencyModels = entities.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return new PageImpl<>(currencyModels, entities.getPageable(), entities.getTotalElements());
    }
}
