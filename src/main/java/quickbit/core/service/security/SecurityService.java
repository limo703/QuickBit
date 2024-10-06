package quickbit.core.service.security;

import com.sun.istack.NotNull;
import quickbit.dbcore.entity.User;

public interface SecurityService {
    void changeContextUser(@NotNull User user);
}
