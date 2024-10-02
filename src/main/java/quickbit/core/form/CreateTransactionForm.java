package quickbit.core.form;

public class CreateTransactionForm {

    private Double amount;
    private Double price;
    private String currencyName;
    private Boolean typeOpp;

    public Double getAmount() {
        return amount;
    }

    public CreateTransactionForm setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public CreateTransactionForm setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public CreateTransactionForm setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public Boolean getTypeOpp() {
        return typeOpp;
    }

    public CreateTransactionForm setTypeOpp(Boolean typeOpp) {
        this.typeOpp = typeOpp;
        return this;
    }
}
