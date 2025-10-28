package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

@Builder
public record GroupTeacherCardResponse(

        Long id,
        String name,
        String status,
        String startDateTime,
        int numberOfStudents,
        int maxStudents,
        Integer minStudents
) {
}
