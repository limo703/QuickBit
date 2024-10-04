package quickbit.core.form;

public class EditUserForm {

    private String firstName;
    private String lastName;
    private String email;
    private String username;

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

    public String getEmail() {
        return email;
    }

    public EditUserForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public EditUserForm setUsername(String username) {
        this.username = username;
        return this;
    }
}
