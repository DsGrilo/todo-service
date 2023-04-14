package com.grilo.todoservice.security.jwt;

import com.grilo.todoservice.architecture.commom.Role;
import com.grilo.todoservice.architecture.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterToken extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;

        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            token = authorizationHeader.replace("Bearer ", "");
            var subject = tokenService.getSubject(token);
            var user = userRepository.findByUsername(subject);
            var authorities =  new ArrayList<GrantedAuthority>();

            switch (user.get().getRole()){
                case ADMIN -> {
                    authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
                    authorities.add(new SimpleGrantedAuthority(Role.CUSTOMER.name()));
                }
                case CUSTOMER -> {
                    authorities.add(new SimpleGrantedAuthority(Role.CUSTOMER.name()));
                }
            }

            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
