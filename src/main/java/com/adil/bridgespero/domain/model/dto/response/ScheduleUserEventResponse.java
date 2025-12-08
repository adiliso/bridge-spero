package com.adil.bridgespero.domain.model.dto.response;

public record ScheduleUserEventResponse(

        Long id,
        Long groupId,
        Long teacherId,
        String name,
        String status,
        String startTime,
        String teacher,
        String joinUrl,
        boolean isMeetingActive
) {
}
