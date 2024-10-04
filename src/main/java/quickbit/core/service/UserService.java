package quickbit.core.service;

import quickbit.core.form.CreateUserForm;
import quickbit.core.form.EditUserForm;
import quickbit.dbcore.entity.User;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    User getById(@NotNull Long id);

    @NotNull
    User editUser(
        @NotNull EditUserForm form,
        @NotNull Long userId
    );

    User getByUsername(@NotNull String username);

    @Nullable
    User getByEmail(@NotNull String email);

    @Nullable
    Optional<User> findByUsername(@NotNull String username);

    @NotNull
    User create(@NotNull CreateUserForm form);

    @NotNull
    Page<User> findAll(@NotNull Pageable pageable);

    @NotNull
    User getByUuid(@NotNull String uuid);
}
