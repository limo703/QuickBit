package quickbit.core.form;

import quickbit.dbcore.entity.CurrencyType;

public class DepositUserForm {

    private Double score;
    private CurrencyType currencyType;

    public Double getScore() {
        return score;
    }

    public DepositUserForm setScore(Double score) {
        this.score = score;
        return this;
    }

    public CurrencyType getCurrency() {
        return currencyType;
    }

    public DepositUserForm setCurrency(CurrencyType currencyType) {
        this.currencyType = currencyType;
        return this;
    }
}
