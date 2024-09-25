package quickbit.dbcore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbit.dbcore.entity.CurrencyPrice;

import java.util.Optional;

public interface CurrencyPriceRepository extends JpaRepository<CurrencyPrice, Long> {

    Optional<CurrencyPrice> findTopByCurrencyId(Long currencyId);
}
