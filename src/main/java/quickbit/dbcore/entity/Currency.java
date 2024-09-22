package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Currency extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CurrencyType name;

    @Column(name = "avatar_id")
    private Long avatar;

    public CurrencyType getName() {
        return name;
    }

    public Currency setName(CurrencyType name) {
        this.name = name;
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
