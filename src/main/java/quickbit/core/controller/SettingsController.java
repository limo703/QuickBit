package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.form.EditUserForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.UserService;
import quickbit.core.util.RedirectUtil;

@Controller
@RequestMapping("setting")
public class SettingsController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public SettingsController(
        UserService userService,
        UserModelAssembler userModelAssembler
    ) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping("profile/edit")
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView getEditUserPage(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        UserModel userModel = userModelAssembler.toModel(authUser.getUser());

        return new ModelAndView("user/edit")
            .addObject("userModel", userModel)
            .addObject("editUserForm",
                new EditUserForm()
                    .setLastName(userModel.getLastName())
                    .setFirstName(userModel.getFirstName())
            );
    }

    @PostMapping("edit")
    @PreAuthorize("@permissionService.isAccess(#authUser)")
    public ModelAndView editUser(
        @Validated @ModelAttribute("editUserForm")
        EditUserForm editUserForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.editUser(editUserForm, authUser.getUser().getId());
        return RedirectUtil.redirect("/user/" + authUser.getUser().getUsername());
    }
}
