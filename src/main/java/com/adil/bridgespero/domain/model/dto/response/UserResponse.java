package com.adil.bridgespero.domain.model.dto.response;

import com.adil.bridgespero.domain.model.enums.Role;
import com.adil.bridgespero.domain.model.enums.Status;

import java.time.Instant;
import java.util.List;

public record UserResponse(

        Long id,
        String email,
        String name,
        String surname,
        Role role,
        Status status,
        String phoneCode,
        String phoneNumber,
        String profilePictureUrl,
        String bio,
        List<Long> interestCategoryIds,
        Instant createdAt,
        Instant updatedAt
) {
}
