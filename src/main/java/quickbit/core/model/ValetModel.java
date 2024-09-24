package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;

public class ValetModel extends RepresentationModel<ValetModel> {

    private Long score;
    private String currencyName;

    public Long getScore() {
        return score;
    }

    public ValetModel setScore(Long score) {
        this.score = score;
        return this;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public ValetModel setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }
}
