package com.adil.bridgespero.domain.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(

        @Schema(nullable = true)
        Long parentId,

        @NotBlank(message = "Name is required")
        String name
) {
}
