package com.grilo.todoservice.service;

import com.grilo.todoservice.architecture.commom.GenericException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final WebSecurity webSecurity;
    private final TokenService tokenService;

    public void createUser(UserSaveModel model){
        var user = model.getId() == 0 ? new User():
                userRepository.findById(model.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(model.getId() == 0){
            var password = webSecurity.getPasswordEncoder().encode(model.getPassword());
            user.setPassword(password);
        }

        user.setName(model.getName());
        user.setUsername(model.getUsername());
        user.setRole(model.getRole());

        userRepository.save(user);
    }

    public UserFindModel verifyLogin(UserLoginModel model, HttpServletResponse httpServletResponse){
       var user = userRepository.findByUsername(model.getUsername());

       if(user.isEmpty()) throw new UsernameNotFoundException("Username or Password Incorrect");
       if(!webSecurity.getPasswordEncoder().matches(model.getPassword(), user.get().getPassword())) throw new BadCredentialsException("Username or Password Incorrect");
       if(!user.get().isEnable()) throw new GenericException("User is disabled");

        httpServletResponse.addHeader("Authorization", tokenService.generateJWT(user.get()));

        return mapper.convert(user.get(), UserFindModel.class);
    }

    public UserFindModel findById(int id){
        var user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!user.isEnable()){
            throw new GenericException("User is disabled");
        }


        return mapper.convert(user, UserFindModel.class);
    }

    public List<UserFindModel> findAll() {
        return  userRepository.findAll().stream()
                .map($ -> mapper.convert($, UserFindModel.class))
                .collect(Collectors.toList());
    }
}
