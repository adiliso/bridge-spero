package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

@Builder
public record TeacherCardResponse(

        Long id,
        String profilePictureName,
        String name,
        String surname,
        Double rating,
        Integer activeStudents,
        String experience
) {
}
