package com.adil.bridgespero.domain.model.dto.response;

public record ScheduleResponse(

        String dayOfWeek,
        String startTime,
        String endTime
) {
}
