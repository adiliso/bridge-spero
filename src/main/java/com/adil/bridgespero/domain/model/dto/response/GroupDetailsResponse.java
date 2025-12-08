package com.adil.bridgespero.domain.model.dto.response;

public record GroupDetailsResponse(

        Long id,
        String name,
        String category,
        String language,
        String description,
        long durationInWeeks,
        int activeStudents,
        Integer maxStudents,
        String startDate,
        Double price,
        String imageUrl,
        TeacherDetailedCardResponse teacher
) {
}
