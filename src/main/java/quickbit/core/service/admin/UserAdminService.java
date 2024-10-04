package quickbit.core.service.admin;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import quickbit.core.form.EditUserForm;
import quickbit.core.model.UserModel;

public interface UserAdminService {

    UserModel getByUuid(@NotNull String uuid);

    Page<UserModel> findAll(
        @NotNull Pageable pageable
    );

    void edit(
        @NotNull EditUserForm form,
        @NotNull String uuid
    );
}
