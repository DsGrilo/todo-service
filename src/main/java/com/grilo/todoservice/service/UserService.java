package com.grilo.todoservice.service;

import com.grilo.todoservice.architecture.commom.Role;
import com.grilo.todoservice.architecture.commom.exception.GenericException;
import com.grilo.todoservice.architecture.commom.Mapper;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.user.UserFindModel;
import com.grilo.todoservice.architecture.model.user.UserLoginModel;
import com.grilo.todoservice.architecture.model.user.UserSaveModel;
import com.grilo.todoservice.architecture.repository.user.UserRepository;
import com.grilo.todoservice.security.jwt.TokenService;
import com.grilo.todoservice.security.service.UserDataImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final TokenService tokenService;
    private final UserDataImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserSaveModel model){
        var user = model.getId() == 0 ? new User():
                userRepository.findById(model.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(model.getId() == 0){
            var password = passwordEncoder.encode(model.getPassword());
            user.setPassword(password);
        }

        user.setName(model.getName());
        user.setUsername(model.getUsername());
        user.setRole(model.getRole() == null ? Role.CUSTOMER : Role.ADMIN);

        userRepository.save(user);
    }

    public UserFindModel verifyLogin(UserLoginModel model, HttpServletResponse httpServletResponse){
        var opt = userRepository.findByUsername(model.getUsername());
        if(opt.isEmpty()) throw new UsernameNotFoundException("Username or Password Incorrect");

        var user = opt.get();
        UserDetails details = userDetailsService.loadUserByUsername(model.getUsername());

        if(!passwordEncoder.matches(model.getPassword(), user.getPassword())) throw new BadCredentialsException("Username or Password Incorrect");
        if(!user.isEnable()) throw new GenericException("User is disabled");

        var jwt = tokenService.generateJWT(details, user.getId());
        httpServletResponse.addHeader("Authorization", jwt);

        return mapper.convert(user, UserFindModel.class);
    }

    public User findById(int id){
        var user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!user.isEnable()){
            throw new GenericException("User is disabled");
        }

        return user;
    }

    public User getUser(String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty())
            throw new GenericException("Usuário não encontrado");
        User user = optional.get();
        if (!user.isEnable())
            throw new GenericException("Esse login está bloqueado");
        return user;
    }

    public List<UserFindModel> findAll() {
        return  userRepository.findAll().stream()
                .map($ -> mapper.convert($, UserFindModel.class))
                .collect(Collectors.toList());
    }
}
