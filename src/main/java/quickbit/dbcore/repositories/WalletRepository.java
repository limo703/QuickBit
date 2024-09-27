package quickbit.dbcore.repositories;

import org.springframework.data.jpa.repository.Query;
import quickbit.dbcore.entity.Wallet;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(@NotNull Long userId);

    Optional<Wallet> findByUserIdAndCurrencyId(@NotNull Long userId, @NotNull Long currencyId);

    @Query(value = "" +
        "select w from Wallet w " +
        "join w.user u " +
        "where u.id = :userId " +
        "and u.defaultWalletId != w.id" +
        ""
    )
    Set<Wallet> findAllByUserIdWithoutDefault(@NotNull Long userId);
}
