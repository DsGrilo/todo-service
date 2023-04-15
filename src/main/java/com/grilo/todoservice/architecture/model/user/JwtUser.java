package com.grilo.todoservice.architecture.model.user;

import com.grilo.todoservice.architecture.commom.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class JwtUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final int id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enable;
    private final Role role;

    public JwtUser(int id, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enable, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enable = enable;
        this.role = role;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

}
