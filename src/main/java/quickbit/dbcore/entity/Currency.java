package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
public class Currency extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CurrencyType name;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

    @Column(name = "avatar_id")
    private Long avatar;

    public CurrencyType getName() {
        return name;
    }

    public Currency setName(CurrencyType name) {
        this.name = name;
        return this;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public Currency setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public Currency setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public Long getAvatar() {
        return avatar;
    }

    public Currency setAvatar(Long avatar) {
        this.avatar = avatar;
        return this;
    }
}
