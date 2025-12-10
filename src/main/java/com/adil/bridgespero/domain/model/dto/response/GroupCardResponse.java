package com.adil.bridgespero.domain.model.dto.response;

import com.adil.bridgespero.domain.model.enums.GroupStatus;

public record GroupCardResponse(

        Long id,
        Long teacherId,
        String imageUrl,
        String category,
        String language,
        String name,
        String teacherName,
        String teacherSurname,
        Double price,
        GroupStatus status
) {
}
