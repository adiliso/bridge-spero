package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record GroupScheduleCardResponse(

        String groupName,
        List<String> times
) {
}
