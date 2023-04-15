package com.grilo.todoservice.security.config;

import com.grilo.todoservice.security.jwt.FilterToken;
import com.grilo.todoservice.security.service.UserDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurity {

    private final FilterToken filterToken;
    private final UserDataImpl implUser;


    public WebSecurity(FilterToken filterToken, UserDataImpl implUser) {
        this.filterToken = filterToken;
        this.implUser = implUser;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity.httpBasic()
               .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
               .authorizeHttpRequests()
               .requestMatchers(HttpMethod.POST, "user/register").permitAll()
               .requestMatchers(HttpMethod.POST, "user/login").permitAll()
               .anyRequest()
               .authenticated().and()
               .addFilterBefore(filterToken, UsernamePasswordAuthenticationFilter.class)
               .csrf().disable().build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }



}
