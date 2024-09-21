package quickbit.dbcore.repositories;

import quickbit.dbcore.entity.Valet;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValetRepository extends JpaRepository<Valet, Long> {

    Optional<Valet> findByUserId(@NotNull Long userId);
}
