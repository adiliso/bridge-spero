package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupTeacherCardResponse(

        String name,
        String status,
        double price,
        String startDate,
        int numberOfStudents,
        int maxStudents,
        Integer minStudents,
        List<String> schedule
) {
}
