package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class WalletModel extends RepresentationModel<WalletModel> {

    private Double score;
    private String currency;

    public Double getScore() {
        return score;
    }

    public WalletModel setScore(Double score) {
        this.score = score;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public WalletModel setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
