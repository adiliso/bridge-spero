package com.adil.bridgespero.security.model;

import com.adil.bridgespero.domain.model.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserPrincipal extends User {

    private String auth;
    private String tokenType;
    private final String fullName;
    private final Role role;

    public CustomUserPrincipal(String email, String fullName, String tokenType, String auth, Role role,
                               Collection<? extends GrantedAuthority> authorities) {
        super(email, "", true, true, true, true, authorities);
        this.fullName = fullName;
        this.tokenType = tokenType;
        this.auth = auth;
        this.role = role;
    }

    public CustomUserPrincipal(String username, String password, String fullName, boolean enabled, Role role,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.fullName = fullName;
        this.role = role;
    }

}
