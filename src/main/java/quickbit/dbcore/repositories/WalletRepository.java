package quickbit.dbcore.repositories;

import quickbit.dbcore.entity.Wallet;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(@NotNull Long userId);

    Optional<Wallet> findByUserIdAndCurrencyId(@NotNull Long userId, @NotNull Long currencyId);
}
