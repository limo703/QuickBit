package quickbit.dbcore.repositories;

import quickbit.dbcore.entity.Image;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUuid(@NotNull String uuid);
}
