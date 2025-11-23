package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.UserNotEnabledException;
import com.adil.bridgespero.exception.UserNotFoundException;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        final UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!userEntity.getEnabled()) throw new UserNotEnabledException();

        List<SimpleGrantedAuthority> authorities
                = List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()));

        return new CustomUserPrincipal(email,
                userEntity.getPassword(),
                getFullName(userEntity),
                userEntity.getEnabled(),
                userEntity.getRole(),
                authorities);
    }

    private String getFullName(UserEntity userEntity) {
        return String.join(" ", userEntity.getName(), userEntity.getSurname());
    }
}
