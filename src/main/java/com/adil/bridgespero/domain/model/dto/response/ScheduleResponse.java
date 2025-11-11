package com.adil.bridgespero.domain.model.dto.response;

public record ScheduleResponse(

        Long id,
        String dayOfWeek,
        String startTime,
        String endTime
) {
}
