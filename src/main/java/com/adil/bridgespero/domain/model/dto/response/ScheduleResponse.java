package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record ScheduleResponse(

        String date,
        List<ScheduleDayResponse> schedule
) {
}
