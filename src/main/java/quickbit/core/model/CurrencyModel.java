package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyModel extends RepresentationModel<CurrencyModel> {

    private String name;
    private String avatar;
    private boolean isFiat;
    private Map<String, Double> pricesMap = new TreeMap<>();

    public String getName() {
        return name;
    }

    public CurrencyModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public CurrencyModel setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isFiat() {
        return isFiat;
    }

    public CurrencyModel setFiat(boolean fiat) {
        isFiat = fiat;
        return this;
    }

    public Map<String, Double> getPricesMap() {
        return pricesMap;
    }

    public CurrencyModel setPricesMap(Map<String, Double> pricesMap) {
        this.pricesMap = pricesMap;
        return this;
    }
}
