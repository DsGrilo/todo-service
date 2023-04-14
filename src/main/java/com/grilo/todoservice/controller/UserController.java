package com.grilo.todoservice.controller;


import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.user.UserCreateModel;
import com.grilo.todoservice.architecture.model.user.UserFindModel;
import com.grilo.todoservice.architecture.model.user.UserLoginModel;
import com.grilo.todoservice.architecture.model.user.UserSaveModel;
import com.grilo.todoservice.security.jwt.TokenService;
import com.grilo.todoservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Mapper mapper;

    @PostMapping("/register")
    public void create(@RequestBody UserCreateModel model){
        var user = mapper.convert(model, UserSaveModel.class);
        userService.createUser(user);
    }

    @PostMapping("/login")
    public UserFindModel login(@RequestBody UserLoginModel model, HttpServletResponse httpServletResponse){
        return userService.verifyLogin(model, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/edit")
    public void edit(@RequestBody UserSaveModel model){
        userService.createUser(model);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/find/{id}")
    public UserFindModel findById(@PathVariable("id") int id){
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/find")
    public List<UserFindModel> findAll(){
        return userService.findAll();
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String check(){
        return "OK";
    }

}
