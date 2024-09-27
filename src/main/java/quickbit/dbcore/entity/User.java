package quickbit.dbcore.entity;

import quickbit.core.util.UserRole;
import quickbit.dbcore.entity.base.UuidTimedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends UuidTimedEntity {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private Long defaultWalletId;

    @Column(name = "avatar_image_id")
    private Long avatarId;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "user"
    )
    private Set<Wallet> wallets = new HashSet<>();

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public User setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public User setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
        return this;
    }

    public Long getDefaultWalletId() {
        return defaultWalletId;
    }

    public User setDefaultWalletId(Long defaultWalletId) {
        this.defaultWalletId = defaultWalletId;
        return this;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public User setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
        return this;
    }
}
