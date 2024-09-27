package quickbit.core.form;

public class DepositForm {

    private Double amount;
    private String currency;

    public Double getAmount() {
        return amount;
    }

    public DepositForm setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public DepositForm setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
