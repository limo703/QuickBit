package quickbit.dbcore.entity;

import org.springframework.data.redis.core.RedisHash;
import quickbit.core.util.TransactionType;
import quickbit.dbcore.entity.base.BaseEntity;

import java.io.Serializable;

@RedisHash("Transaction")
public class Transaction extends BaseEntity implements Serializable {

    private Long userId;
    private Double amount;
    private Double operationPrice;
    private Long purchaseCurrencyId;
    private Long sellCurrencyId;

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

    public Long getPurchaseCurrencyId() {
        return purchaseCurrencyId;
    }

    public Transaction setPurchaseCurrencyId(Long purchaseCurrencyId) {
        this.purchaseCurrencyId = purchaseCurrencyId;
        return this;
    }

    public Long getSellCurrencyId() {
        return sellCurrencyId;
    }

    public Transaction setSellCurrencyId(Long sellCurrencyId) {
        this.sellCurrencyId = sellCurrencyId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Transaction setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
