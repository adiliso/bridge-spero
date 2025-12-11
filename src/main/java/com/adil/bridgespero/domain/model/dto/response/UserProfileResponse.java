package com.adil.bridgespero.domain.model.dto.response;

public record UserProfileResponse(

        Long id,
        String name,
        String surname,
        String bio,
        String imageUrl,
        String backgroundImageUrl
) {
}
