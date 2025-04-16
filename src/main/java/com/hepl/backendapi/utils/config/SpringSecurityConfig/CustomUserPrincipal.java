package com.hepl.backendapi.utils.config.SpringSecurityConfig;

import com.hepl.backendapi.entity.dbtransac.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String name;
    private final String phone;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null; // Pas utilisé ici car JWT
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

    public static CustomUserPrincipal fromEntity(UserEntity user, Collection<? extends GrantedAuthority> authorities) {
        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getRole(),
                authorities
        );
    }
}