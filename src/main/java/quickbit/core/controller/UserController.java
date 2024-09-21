//package com.example.petProject.core.controller;
//
//import com.example.petProject.core.model.AuthUser;
//import com.example.petProject.core.model.UserModel;
//import com.example.petProject.core.model.assembler.UserModelAssembler;
//import com.example.petProject.core.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping("users")
//public class UserController {
//
//    private final UserService userService;
//    private final UserModelAssembler userModelAssembler;
//
//    @Autowired
//    public UserController(
//        UserService userService,
//        UserModelAssembler userModelAssembler
//    ) {
//        this.userService = userService;
//        this.userModelAssembler = userModelAssembler;
//    }
//
//    @GetMapping()
//    @PreAuthorize("@permissionService.isAuth(#authUser)")
//    public ModelAndView getAllUsers(
//        @PageableDefault Pageable pageable,
//        @AuthenticationPrincipal AuthUser authUser
//    ) {
//        Page<UserModel> users = userModelAssembler.toPagedModel(
//                userService.findAll(pageable)
//        );
//
//        return new ModelAndView("user/users")
//            .addObject("users", users);
//    }
//}
