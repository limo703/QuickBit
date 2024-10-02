package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class WalletModel extends RepresentationModel<WalletModel> {

    private Double amount;
    private Double reservedAmount;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public WalletModel setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public WalletModel setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getReservedAmount() {
        return reservedAmount;
    }

    public WalletModel setReservedAmount(Double reservedAmount) {
        this.reservedAmount = reservedAmount;
        return this;
    }


}
