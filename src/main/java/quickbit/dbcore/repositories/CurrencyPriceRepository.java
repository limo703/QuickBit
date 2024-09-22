package quickbit.dbcore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbit.dbcore.entity.CurrencyPrice;

public interface CurrencyPriceRepository extends JpaRepository<CurrencyPrice, Long> {
}
