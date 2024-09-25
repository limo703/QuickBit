package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Currency extends BaseEntity {

    private String name;
    private String description;
    @Column(name = "avatar_id")
    private Long avatar;
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "currency"
    )
    private Set<CurrencyPrice> prices = new HashSet<>();

    private boolean isFiat;

    public Long getAvatar() {
        return avatar;
    }

    public Currency setAvatar(Long avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getName() {
        return name;
    }

    public Currency setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Currency setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<CurrencyPrice> getPrices() {
        return prices;
    }

    public Currency setPrices(Set<CurrencyPrice> prices) {
        this.prices = prices;
        return this;
    }

    public boolean isFiat() {
        return isFiat;
    }

    public Currency setFiat(boolean fiat) {
        isFiat = fiat;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return
            isFiat == currency.isFiat
            && Objects.equals(name, currency.name)
            && Objects.equals(description, currency.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, isFiat);
    }
}
