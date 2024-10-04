package quickbit.core.service.admin;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quickbit.core.form.EditUserForm;
import quickbit.core.model.UserModel;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.UserService;
import quickbit.dbcore.entity.User;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserAdminServiceImpl(
        UserService userService,
        UserModelAssembler userModelAssembler
    ) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @Override
    public UserModel getByUuid(@NotNull String uuid) {
        return userModelAssembler.toModel(
            userService.getByUuid(uuid)
        );
    }

    @Override
    public Page<UserModel> findAll(
        @NotNull Pageable pageable
    ) {
        return userModelAssembler.toModels(
            userService.findAll(pageable)
        );
    }

    @Override
    public void edit(
        @NotNull EditUserForm form,
        @NotNull String uuid
    ) {
        User user = userService.getByUuid(uuid);
        userService.editUser(form, user.getId());
    }
}
