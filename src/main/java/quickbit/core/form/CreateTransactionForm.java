package quickbit.core.form;

public class CreateTransactionForm {

    private Double amount;
    private Double price;
    private String purchaseCurrencyName;
    private String sellCurrencyName;

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

    public String getPurchaseCurrencyName() {
        return purchaseCurrencyName;
    }

    public CreateTransactionForm setPurchaseCurrencyName(String purchaseCurrencyName) {
        this.purchaseCurrencyName = purchaseCurrencyName;
        return this;
    }

    public String getSellCurrencyName() {
        return sellCurrencyName;
    }

    public CreateTransactionForm setSellCurrencyName(String sellCurrencyName) {
        this.sellCurrencyName = sellCurrencyName;
        return this;
    }
}
