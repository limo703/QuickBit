package quickbit.core.form;

public class EditUserForm {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public EditUserForm setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EditUserForm setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
