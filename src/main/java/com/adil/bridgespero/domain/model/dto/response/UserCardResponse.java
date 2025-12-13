package com.adil.bridgespero.domain.model.dto.response;

import com.adil.bridgespero.domain.model.enums.Role;

public record UserCardResponse(

        Long id,
        String name,
        String surname,
        Role role
) {
}
