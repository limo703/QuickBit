package quickbit.core.model;

import org.springframework.hateoas.RepresentationModel;
import quickbit.dbcore.entity.User;

public class UserModel extends RepresentationModel<UserModel> {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private WalletModel wallet;
    private String uuid;
    private String avatarUuid;

    public String getUsername() {
        return username;
    }

    public UserModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public UserModel setWallet(WalletModel wallet) {
        this.wallet = wallet;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public UserModel setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getAvatar() {
        return avatarUuid;
    }

    public UserModel setAvatar(String avatar) {
        this.avatarUuid = avatar;
        return this;
    }

    public String getAvatarUuid() {
        return avatarUuid;
    }

    public UserModel setAvatarUuid(String avatarUuid) {
        this.avatarUuid = avatarUuid;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public static UserModel of(User user) {
        return new UserModel()
            .setUsername(user.getUsername())
            .setFirstName(user.getFirstName())
            .setLastName(user.getLastName())
            .setEmail(user.getEmail())
            .setUuid(user.getUuid());
    }
}
