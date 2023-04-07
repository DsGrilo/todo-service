package com.grilo.todoservice.controller;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;


    @PostMapping("/register")
    public UserFindModel create(@RequestBody UserSaveModel model){
        return userService.createUser(model);
    }

    @PostMapping("/login")
    public UserFindModel login(@RequestBody UserLoginModel model, HttpServletResponse httpServletResponse){
        return userService.verifyLogin(model, httpServletResponse);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String check(){
        return "OK";
    }

}
