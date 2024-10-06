package quickbit.core.service.security;

import com.sun.istack.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import quickbit.core.model.AuthUser;
import quickbit.dbcore.entity.User;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public void changeContextUser(@NotNull User user) {
        AuthUser authUser = new AuthUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
