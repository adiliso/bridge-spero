package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.UserNotEnabledException;
import com.adil.bridgespero.exception.UserNotFoundException;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomUserPrincipal loadUserByUsername(String email) {
        final UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!userEntity.getEnabled()) throw new UserNotEnabledException();

        return new CustomUserPrincipal(email,
                userEntity.getPassword(),
                getFullName(userEntity),
                userEntity.getEnabled(),
                userEntity.getRole(),
                Collections.emptyList());
    }

    private String getFullName(UserEntity userEntity) {
        return String.join(" ", userEntity.getName(), userEntity.getSurname());
    }
}
