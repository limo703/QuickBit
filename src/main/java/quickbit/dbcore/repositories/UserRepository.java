package quickbit.dbcore.repositories;

import quickbit.dbcore.entity.User;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@NotNull String username);

    Optional<User> findByEmail(@NotNull String username);
}
