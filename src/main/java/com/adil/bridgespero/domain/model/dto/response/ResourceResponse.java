package com.adil.bridgespero.domain.model.dto.response;

public record ResourceResponse(

        Long id,
        String name,
        String contentType,
        String createdAt
) {
}
