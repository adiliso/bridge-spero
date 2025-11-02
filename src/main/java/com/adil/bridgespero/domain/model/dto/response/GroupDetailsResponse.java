package com.adil.bridgespero.domain.model.dto.response;

public record GroupDetailsResponse(
        String name,
        String category,
        String language,
        String description,
        long durationInWeeks,
        int activeStudents,
        Integer maxStudents,
        String startDate,
        Double price,
        TeacherCardResponse teacher
) {
}
