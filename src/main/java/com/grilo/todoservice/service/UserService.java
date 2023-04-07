package com.grilo.todoservice.service;

import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.commom.Role;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.user.UserFindModel;
import com.grilo.todoservice.architecture.model.user.UserLoginModel;
import com.grilo.todoservice.architecture.model.user.UserSaveModel;
import com.grilo.todoservice.architecture.repository.user.UserRepository;
import com.grilo.todoservice.security.config.WebSecurity;
import com.grilo.todoservice.security.jwt.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final WebSecurity webSecurity;
    private final TokenService tokenService;

    public UserFindModel createUser(UserSaveModel model){
        var user = new User();
        var password = webSecurity.getPasswordEncoder().encode(model.getPassword());
        user = mapper.convert(model, User.class);


        if(model.getRole() == null) user.setRole(Role.CUSTOMER);

        user.setPassword(password);

        userRepository.save(user);
        return mapper.convert(user, UserFindModel.class);
    }

    public UserFindModel verifyLogin(UserLoginModel model, HttpServletResponse httpServletResponse){
       var user = userRepository.findByUsername(model.getUsername());

       if(user.isEmpty()) throw new UsernameNotFoundException("Username or Password Incorrect");
       if(!webSecurity.getPasswordEncoder().matches(model.getPassword(), user.get().getPassword())) throw new BadCredentialsException("Username or Password Incorrect");

        httpServletResponse.addHeader("Authorization", tokenService.generateJWT(user.get()));

        return mapper.convert(user.get(), UserFindModel.class);
    }

}
