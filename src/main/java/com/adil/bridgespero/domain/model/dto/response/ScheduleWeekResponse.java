package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record ScheduleWeekResponse(

        String date,
        List<ScheduleDayResponse> schedule
) {
}
