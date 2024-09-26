package quickbit.dbcore.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import quickbit.dbcore.entity.CurrencyPrice;

import java.util.Optional;
import java.util.Set;

public interface CurrencyPriceRepository extends JpaRepository<CurrencyPrice, Long> {

    Optional<CurrencyPrice> findTopByCurrencyId(@NotNull Long currencyId);

    Set<CurrencyPrice> findAllByCurrencyId(@NotNull Long currencyId);
}
