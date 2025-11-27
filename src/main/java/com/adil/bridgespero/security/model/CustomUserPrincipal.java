package com.adil.bridgespero.security.model;

import com.adil.bridgespero.domain.model.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserPrincipal extends User {

    private final Long id;
    private String auth;
    private String tokenType;
    private final String fullName;
    private final Role role;

    public CustomUserPrincipal(Long id, String email, String fullName, String tokenType, String auth, Role role,
                               Collection<? extends GrantedAuthority> authorities) {
        super(email, "", true, true, true, true, authorities);
        this.id = id;
        this.fullName = fullName;
        this.tokenType = tokenType;
        this.auth = auth;
        this.role = role;
    }

    public CustomUserPrincipal(Long id, String email, String password, String fullName, boolean enabled, Role role,
                               Collection<? extends GrantedAuthority> authorities) {
        super(email, password, enabled, true, true, true, authorities);
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }

}
