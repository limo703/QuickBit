package quickbit.core.form.admin;

public class EditCurrencyForm extends CreateCurrencyForm {

    private String currencyName;

    public String getCurrencyName() {
        return currencyName;
    }

    public EditCurrencyForm setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }
}
