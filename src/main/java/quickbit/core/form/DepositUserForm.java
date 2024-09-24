package quickbit.core.form;

import quickbit.dbcore.entity.Currency;

public class DepositUserForm {

    private Double score;
    private String currencyName;

    public Double getScore() {
        return score;
    }

    public DepositUserForm setScore(Double score) {
        this.score = score;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public DepositUserForm setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }
}
