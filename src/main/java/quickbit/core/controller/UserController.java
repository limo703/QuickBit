package quickbit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import quickbit.core.form.EditUserForm;
import quickbit.core.model.AuthUser;
import quickbit.core.model.UserModel;
import quickbit.core.model.assembler.UserModelAssembler;
import quickbit.core.service.ImageService;
import quickbit.core.service.UserService;
import quickbit.core.service.WalletService;
import quickbit.core.util.RedirectUtil;
import quickbit.core.validator.DepositFormValidator;

@Controller
@RequestMapping("user/{username}")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final UserModelAssembler assembler;
    private final WalletService walletService;
    private final DepositFormValidator depositFormValidator;

    @Autowired
    public UserController(
        UserService userService,
        ImageService imageService,
        UserModelAssembler assembler, WalletService walletService,
        DepositFormValidator depositFormValidator
    ) {
        this.userService = userService;
        this.imageService = imageService;
        this.assembler = assembler;
        this.walletService = walletService;
        this.depositFormValidator = depositFormValidator;
    }

    @InitBinder("depositUserForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(depositFormValidator);
    }

    @GetMapping
    @PreAuthorize("@permissionService.check(#authUser)")
    public ModelAndView getUserPage(
        UserModel userModel,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Boolean isCurrentUser = authUser.getUser().getUsername().equals(userModel.getUsername());

        return new ModelAndView("user/user")
            .addObject("userModel", userModel)
            .addObject("isCurrentUser", isCurrentUser);
    }

    @GetMapping("edit")
    @PreAuthorize("@permissionService.check(#authUser, #userModel)")
    public ModelAndView getEditUserPage(
        UserModel userModel,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        return new ModelAndView("user/edit")
            .addObject("userModel", userModel)
            .addObject("editUserForm", new EditUserForm());
    }

    @PostMapping("update-avatar")
    @PreAuthorize("@permissionService.check(#authUser, #userModel)")
    public ModelAndView updateAvatar(
        UserModel userModel,
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        imageService.uploadImage(file, authUser.getUser());
        return RedirectUtil.redirect("/user/" + authUser.getUser().getUsername());
    }

    @PostMapping("edit")
    @PreAuthorize("@permissionService.check(#authUser, #userModel)")
    public ModelAndView editUser(
        UserModel userModel,
        @Validated @ModelAttribute("editUserForm")
        EditUserForm editUserForm,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.editUser(editUserForm, authUser.getUser().getId());
        return RedirectUtil.redirect("/user/" + authUser.getUser().getUsername());
    }
}
