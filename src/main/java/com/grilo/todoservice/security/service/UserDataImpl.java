package com.grilo.todoservice.security.service;

import com.grilo.todoservice.architecture.commom.Role;
import com.grilo.todoservice.architecture.entity.user.User;
import com.grilo.todoservice.architecture.model.user.JwtUser;
import com.grilo.todoservice.architecture.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.grilo.todoservice.architecture.commom.Role.ADMIN;


@Service
@RequiredArgsConstructor
public class UserDataImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return create(user);
    }

    public JwtUser create(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (user.getRole()){
            case ADMIN -> {
                authorities.add(new SimpleGrantedAuthority(ADMIN.name()));
                authorities.add(new SimpleGrantedAuthority(Role.CUSTOMER.name()));
            }
            case CUSTOMER -> {
                authorities.add(new SimpleGrantedAuthority(Role.CUSTOMER.name()));
            }
        }

        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), authorities, user.isEnable(), user.getRole());
    }
}
