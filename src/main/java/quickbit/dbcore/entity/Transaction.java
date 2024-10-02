package quickbit.dbcore.entity;

import org.springframework.data.redis.core.RedisHash;
import quickbit.core.util.TransactionType;
import quickbit.dbcore.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
public class Transaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    private Double amount;
    private Double operationPrice;
    private Long currencyId;
    private Boolean typeOpp;

    public Double getAmount() {
        return amount;
    }

    public Transaction setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getOperationPrice() {
        return operationPrice;
    }

    public Transaction setOperationPrice(Double operationPrice) {
        this.operationPrice = operationPrice;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Transaction setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public Transaction setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public Boolean getTypeOpp() {
        return typeOpp;
    }

    public Transaction setTypeOpp(Boolean typeOpp) {
        this.typeOpp = typeOpp;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Transaction setUser(User user) {
        this.user = user;
        return this;
    }
}
