package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class WalletModel extends RepresentationModel<WalletModel> {

    private Double score;
    private String currencyName;

    public Double getScore() {
        return score;
    }

    public WalletModel setScore(Double score) {
        this.score = score;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public WalletModel setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }
}
