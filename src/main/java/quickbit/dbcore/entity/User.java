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

    @Column(name = "avatar_image_id")
    private Long avatarId;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "user"
    )
    private Set<Wallet> wallet = new HashSet<>();

    @Column(name = "wallet_id")
    private Long walletId;

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

    public Long getWalletId() {
        return walletId;
    }

    public User setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public Set<Wallet> getWallet() {
        return wallet;
    }

    public User setWallet(Set<Wallet> wallet) {
        this.wallet = wallet;
        return this;
    }
}
