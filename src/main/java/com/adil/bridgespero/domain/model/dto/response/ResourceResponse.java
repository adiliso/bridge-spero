package com.adil.bridgespero.domain.model.dto.response;

public record ResourceResponse(

        Long id,
        String name,
        String path,
        String contentType,
        String createdAt
) {
}
