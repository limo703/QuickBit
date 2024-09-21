package quickbit.core.service.security;


import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("permissionService")
public class PermissionService {

    public boolean isAccess(AuthUser authUser) {
        return Objects.nonNull(authUser);
    }

    public boolean isAccess(AuthUser authUser, UserModel userModel) {
        return isAccess(authUser) && Objects.equals(authUser.getUsername(), userModel.getUsername());
    }

}
