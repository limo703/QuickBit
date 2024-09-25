package quickbit.core.service;

import quickbit.core.form.CreateUserForm;
import quickbit.core.form.EditUserForm;
import quickbit.dbcore.entity.User;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.Optional;

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

    Optional<User> findByUsername(@NotNull String username);

    User create(@NotNull CreateUserForm form);

    Page<User> findAll(@NotNull Pageable pageable);
}