package quickbit.core.controller;

import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.UserService;
import quickbit.dbcore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    public static final String USER_MODEL = "userModel";

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserControllerAdvice(
        UserService userService,
        UserModelAssembler userModelAssembler
    ) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @ModelAttribute
    public void aliasAttribute(
        @PathVariable String username,
        Model model
    ) {
        User user = userService.getByUsername(username);

        model.addAttribute(USER_MODEL, userModelAssembler.toModel(user));
    }
}
