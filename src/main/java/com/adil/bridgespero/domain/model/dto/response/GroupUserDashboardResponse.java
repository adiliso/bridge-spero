package com.adil.bridgespero.domain.model.dto.response;

public record GroupUserDashboardResponse(

        Long id,
        Long teacherId,
        String name,
        String status,
        String startTime,
        String teacher
) {
}
