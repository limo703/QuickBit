package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.UuidTimedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Wallet extends UuidTimedEntity {

    private BigDecimal amount;
    private BigDecimal reservedAmount;
    @Column(name = "currency_id", insertable = false, updatable = false)
    private Long currencyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    public BigDecimal getAmount() {
        return amount;
    }

    public Wallet setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Wallet add(BigDecimal amount) {
        this.amount = this.amount.add(amount);
        return this;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public Wallet setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
        return this;
    }
}
