package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.UuidTimedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Valet extends UuidTimedEntity {

    private Long score;

    private Currency currency;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    public Long getScore() {
        return score;
    }

    public Valet setScore(Long score) {
        this.score = score;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Valet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Valet setUser(User user) {
        this.user = user;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Valet setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
