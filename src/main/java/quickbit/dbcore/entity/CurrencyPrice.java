package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.BaseEntity;
import quickbit.dbcore.entity.base.TimedEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class CurrencyPrice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "currency_id", insertable = false, updatable = false)
    private Long currencyId;

    private BigDecimal price;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CurrencyPrice setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CurrencyPrice setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public CurrencyPrice setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public CurrencyPrice setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
