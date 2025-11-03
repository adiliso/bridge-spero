package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupStudentCardResponse;
import com.adil.bridgespero.domain.model.dto.response.StudentDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;

import java.util.List;

public class UserMapper {

    public StudentDashboardResponse toDashboardResponse(UserEntity entity, List<GroupStudentCardResponse> groups) {
        return StudentDashboardResponse.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .bio(entity.getBio())
                .activeGroups(getActiveGroups(entity))
                .studyHoursThisWeek(calculateStudyHours(entity))
                .lessonsToday(groups.size())
                .build();
    }

    private int getActiveGroups(UserEntity entity) {
        if (entity.getGroups() == null) return 0;
        return (int) entity.getGroups().stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .count();
    }

    private double calculateStudyHours(UserEntity entity) {
        if (entity.getGroups() == null) return 0.0;
        return entity.getGroups().stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .mapToDouble(g -> g.getLessonSchedules().size() * 2)
                .sum();
    }
}
