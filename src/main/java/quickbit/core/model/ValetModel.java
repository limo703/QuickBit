package quickbit.core.model;

import quickbit.dbcore.entity.CurrencyType;
import org.springframework.hateoas.RepresentationModel;

public class ValetModel extends RepresentationModel<ValetModel> {

    private Long score;

    private CurrencyType currencyType;

    public Long getScore() {
        return score;
    }

    public ValetModel setScore(Long score) {
        this.score = score;
        return this;
    }

    public CurrencyType getCurrency() {
        return currencyType;
    }

    public ValetModel setCurrency(CurrencyType currencyType) {
        this.currencyType = currencyType;
        return this;
    }
}
