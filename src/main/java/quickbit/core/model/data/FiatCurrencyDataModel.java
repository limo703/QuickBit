package quickbit.core.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FiatCurrencyDataModel {
    private String date;
    private String base;

    @JsonProperty("rates")
    private Map<String, Double> rates;

    public String getDate() {
        return date;
    }

    public FiatCurrencyDataModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getBase() {
        return base;
    }

    public FiatCurrencyDataModel setBase(String base) {
        this.base = base;
        return this;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public FiatCurrencyDataModel setRates(Map<String, Double> rates) {
        this.rates = rates;
        return this;
    }
}
