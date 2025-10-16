package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupTeacherCardResponse(

        String name,
        String status,
        String startDate,
        Integer numberOfStudents,
        Integer maxStudents,
        List<String> schedule
) {
}
