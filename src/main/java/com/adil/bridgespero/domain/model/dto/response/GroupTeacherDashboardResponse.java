package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

@Builder
public record GroupTeacherDashboardResponse(

        Long id,
        String name,
        String status,
        String startTime,
        int numberOfStudents,
        int maxStudents,
        String startUrl,
        boolean isMeetingActive
) {
}
