package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

@Builder
public record ScheduleTeacherEventResponse(

        Long id,
        Long groupId,
        String name,
        String status,
        String startTime,
        String endTime,
        int numberOfStudents,
        int maxStudents,
        String startUrl,
        boolean isMeetingActive
) {
}
