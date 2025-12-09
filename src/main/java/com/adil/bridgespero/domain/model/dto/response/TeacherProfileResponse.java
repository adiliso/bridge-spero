package com.adil.bridgespero.domain.model.dto.response;

public record TeacherProfileResponse(

        Long id,
        String name,
        String surname,
        Double rating,
        int students,
        String bio,
        String imageUrl,
        String backgroundImageUrl,
        String demoVideoUrl
) {
}
