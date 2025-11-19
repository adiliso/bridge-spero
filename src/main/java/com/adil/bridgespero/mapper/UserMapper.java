package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupUserDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class UserMapper {

    public UserDashboardResponse toDashboardResponse(UserEntity entity, List<GroupUserDashboardResponse> groups) {
        return new UserDashboardResponse(
                entity.getName(),
                getActiveGroups(entity),
                calculateWeeklyStudyHours(entity),
                groups.size(),
                groups
        );
    }

    private int getActiveGroups(UserEntity entity) {
        if (entity.getGroups() == null) return 0;
        return (int) entity.getGroups().stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .count();
    }

    private double calculateWeeklyStudyHours(UserEntity entity) {
        if (entity.getGroups() == null) return 0.0;
        return entity.getGroups().stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .flatMap(group -> group.getLessonSchedules().stream())
                .mapToDouble(this::calculateDurationInHours)
                .sum();
    }

    private double calculateDurationInHours(LessonScheduleEntity ls) {
        long minutes = Duration.between(ls.getStartTime(), ls.getEndTime()).toMinutes();
        return (double) minutes / 60;
    }
}
