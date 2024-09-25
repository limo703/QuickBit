package quickbit.core.controller;

import quickbit.core.model.AuthUser;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public HomeController(
        UserService userService,
        UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping()
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView home(
        @AuthenticationPrincipal AuthUser authUser
        ) {
        return new ModelAndView("home")
            .addObject("authUser", userModelAssembler.toModel(authUser.getUser()));
    }
}
