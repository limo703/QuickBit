package quickbit.core.form;

public class ExchangeCurrenciesForm {

    private String fromCurrency;
    private String toCurrency;
    private Double amount;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public ExchangeCurrenciesForm setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
        return this;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public ExchangeCurrenciesForm setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExchangeCurrenciesForm setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
