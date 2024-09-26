package quickbit.dbcore.repositories;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import quickbit.dbcore.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByName(@NotNull String name);

    List<Currency> findAllByIsFiatIsTrue();
    List<Currency> findAllByIsFiatIsFalse();

    Page<Currency> findAllByIsFiatIsFalse(@NotNull Pageable pageable);
}
