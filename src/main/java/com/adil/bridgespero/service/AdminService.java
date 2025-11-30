package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.request.AdminCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.AdminResponse;
import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String createAdmin(AdminCreateRequest request) {
        var userDto = UserDto.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.Name())
                .surname(request.Surname())
                .role(Role.ADMIN)
                .enabled(true)
                .agreedToTerms(true)
                .build();
        return userService.save(userDto).getEmail();
    }

    public List<AdminResponse> getAll() {
        return userRepository.findAllByRole(Role.ADMIN)
                .stream()
                .map(userMapper::toAdminResponse)
                .toList();
    }
}
