package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TeacherCardResponse(

        Long id,
        String profilePictureUrl,
        String name,
        String surname,
        Double rating,
        Integer activeStudents,
        List<String> subjects,
        String experience
) {
}
