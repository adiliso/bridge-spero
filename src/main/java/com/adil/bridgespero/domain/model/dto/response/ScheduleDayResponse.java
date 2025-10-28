package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record ScheduleDayResponse(

        String dayOfWeek,
        List<GroupScheduleCardResponse> groups
) {
}
