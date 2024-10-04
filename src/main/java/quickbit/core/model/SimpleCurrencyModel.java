package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class SimpleCurrencyModel extends RepresentationModel<SimpleCurrencyModel> {

    private String name;
    private Double lastPrice;

    public String getName() {
        return name;
    }

    public SimpleCurrencyModel setName(String name) {
        this.name = name;
        return this;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public SimpleCurrencyModel setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }
}
