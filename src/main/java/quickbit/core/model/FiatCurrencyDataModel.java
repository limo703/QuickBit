package quickbit.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FiatCurrencyDataModel {
    private String date;
    private String name;

    public String getDate() {
        return date;
    }

    public FiatCurrencyDataModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getName() {
        return name;
    }

    public FiatCurrencyDataModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<Valute> getValutes() {
        return valutes;
    }

    public FiatCurrencyDataModel setValutes(List<Valute> valutes) {
        this.valutes = valutes;
        return this;
    }

    @JsonProperty("valutes")
    private List<Valute> valutes;

    public static class Valute {

        private String id;

        @JsonProperty("numCode")
        private String numCode;

        @JsonProperty("charCode")
        private String charCode;

        private int nominal;
        private String name;

        @JsonProperty("value")
        private String value;

        @JsonProperty("vunitRate")
        private String vunitRate;

        public String getId() {
            return id;
        }

        public Valute setId(String id) {
            this.id = id;
            return this;
        }

        public String getNumCode() {
            return numCode;
        }

        public Valute setNumCode(String numCode) {
            this.numCode = numCode;
            return this;
        }

        public String getCharCode() {
            return charCode;
        }

        public Valute setCharCode(String charCode) {
            this.charCode = charCode;
            return this;
        }

        public int getNominal() {
            return nominal;
        }

        public Valute setNominal(int nominal) {
            this.nominal = nominal;
            return this;
        }

        public String getName() {
            return name;
        }

        public Valute setName(String name) {
            this.name = name;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Valute setValue(String value) {
            this.value = value;
            return this;
        }

        public String getVunitRate() {
            return vunitRate;
        }

        public Valute setVunitRate(String vunitRate) {
            this.vunitRate = vunitRate;
            return this;
        }
    }
}
