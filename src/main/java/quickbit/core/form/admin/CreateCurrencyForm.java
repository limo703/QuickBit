package quickbit.core.form.admin;

public class CreateCurrencyForm {

    private String name;
    private String description;
    private boolean isFiat;

    public String getName() {
        return name;
    }

    public CreateCurrencyForm setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateCurrencyForm setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isFiat() {
        return isFiat;
    }

    public CreateCurrencyForm setFiat(boolean fiat) {
        isFiat = fiat;
        return this;
    }
}
