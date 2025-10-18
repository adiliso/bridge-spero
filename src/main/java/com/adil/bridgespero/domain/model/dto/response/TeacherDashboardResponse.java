package com.adil.bridgespero.domain.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TeacherDashboardResponse(

        String name,
        Integer activeStudents,
        Double totalEarning,
        Double rating,
        List<GroupTeacherCardResponse> groups
) {
}
