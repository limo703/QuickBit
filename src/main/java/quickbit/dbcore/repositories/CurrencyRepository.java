package quickbit.dbcore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbit.dbcore.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
