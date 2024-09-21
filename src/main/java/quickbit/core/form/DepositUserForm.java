package quickbit.core.form;

import quickbit.dbcore.entity.Currency;

public class DepositUserForm {

    private Double score;
    private Currency currency;

    public Double getScore() {
        return score;
    }

    public DepositUserForm setScore(Double score) {
        this.score = score;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public DepositUserForm setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
