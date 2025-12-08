package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(

        @NotBlank(message = "Name is required")
        String name
) {
}
