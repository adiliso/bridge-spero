package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record UserDashboardResponse(

        String name,
        int activeGroups,
        double studyHours,
        int lessonsToday,
        List<ScheduleUserEventResponse> events
) {
}
