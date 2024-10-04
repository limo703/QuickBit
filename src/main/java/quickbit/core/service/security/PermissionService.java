package quickbit.core.service.security;


import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;
import org.springframework.stereotype.Service;
import quickbit.core.util.UserRole;

import java.util.Objects;

@Service("permissionService")
public class PermissionService {

    public boolean check(AuthUser authUser) {
        return Objects.nonNull(authUser);
    }

    public boolean check(AuthUser authUser, UserModel userModel) {
        return check(authUser) && Objects.equals(authUser.getUsername(), userModel.getUsername());
    }

    public boolean isAdmin(AuthUser authUser) {
        if (check(authUser)) {
            return UserRole.ADMIN.equals(authUser.getUser().getRole());
        }
        return false;
    }

}
