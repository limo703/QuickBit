package quickbit.core.model;

import quickbit.dbcore.entity.Currency;
import org.springframework.hateoas.RepresentationModel;

public class ValetModel extends RepresentationModel<ValetModel> {

    private Long score;

    private Currency currency;

    public Long getScore() {
        return score;
    }

    public ValetModel setScore(Long score) {
        this.score = score;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ValetModel setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
