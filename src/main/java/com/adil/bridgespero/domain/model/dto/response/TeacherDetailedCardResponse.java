package com.adil.bridgespero.domain.model.dto.response;

public record TeacherDetailedCardResponse(

        Long id,
        String profilePictureName,
        String name,
        String surname,
        Double rating,
        Integer activeStudents,
        String experience
) {
}
