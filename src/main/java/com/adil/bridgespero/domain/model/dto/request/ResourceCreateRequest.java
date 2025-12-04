package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ResourceCreateRequest(

        String name,

        @NotNull(message = "File does not exists")
        MultipartFile file
) {
}
