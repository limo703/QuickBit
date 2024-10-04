package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class TransactionModel extends RepresentationModel<CurrencyModel>  {

    private Double amount;
    private Double operationPrice;
    private String currencyName;
    private Boolean typeOpp;
    private UserModel user;

    public Double getAmount() {
        return amount;
    }

    public TransactionModel setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getOperationPrice() {
        return operationPrice;
    }

    public TransactionModel setOperationPrice(Double operationPrice) {
        this.operationPrice = operationPrice;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public TransactionModel setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public Boolean getTypeOpp() {
        return typeOpp;
    }

    public TransactionModel setTypeOpp(Boolean typeOpp) {
        this.typeOpp = typeOpp;
        return this;
    }

    public UserModel getUser() {
        return user;
    }

    public TransactionModel setUser(UserModel user) {
        this.user = user;
        return this;
    }
}
