package quickbit.core.controller.api.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;

@RestController
@RequestMapping("api/user/{username}")
public class UserApiController {

    @GetMapping
    @PreAuthorize("@permissionService.check(#authUser)")
    public UserModel getUserInfo(
        UserModel userModel,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        return userModel;
    }


}
