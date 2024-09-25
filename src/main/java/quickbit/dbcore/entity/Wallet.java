package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.UuidTimedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class Wallet extends UuidTimedEntity {

    private BigDecimal score;
    @Column(name = "currency_id")
    private Long currencyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currecy_id")
    private Currency currency;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    public BigDecimal getScore() {
        return score;
    }

    public Wallet setScore(BigDecimal score) {
        this.score = score;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Wallet setUser(User user) {
        this.user = user;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Wallet setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public Wallet setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Wallet setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
