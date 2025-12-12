package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.ScheduleEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.response.AdminResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleUserEventResponse;
import com.adil.bridgespero.domain.model.dto.response.UserCardResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.UserProfileResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Component
public class UserMapper {

    public UserDashboardResponse toDashboardResponse(UserEntity entity, List<ScheduleUserEventResponse> groups) {
        return new UserDashboardResponse(
                entity.getName(),
                getActiveGroups(entity),
                calculateWeeklyStudyHours(entity),
                groups.size(),
                groups
        );
    }

    public AdminResponse toAdminResponse(UserEntity entity) {
        return new AdminResponse(
                entity.getId(),
                entity.getEmail(),
                String.join(" ", entity.getName(), entity.getSurname())
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
        double hours = entity.getGroups().stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .flatMap(group -> group.getSchedules().stream())
                .mapToDouble(this::calculateDurationInHours)
                .sum();
        return Math.round(hours * 10.0) / 10.0;
    }

    private double calculateDurationInHours(ScheduleEntity ls) {
        LocalTime start = ls.getStartTime();
        LocalTime end = ls.getEndTime();

        long minutes;
        if (!end.isAfter(start)) {
            minutes = Duration.between(start, end).plusHours(24).toMinutes();
        } else {
            minutes = Duration.between(start, end).toMinutes();
        }

        return minutes / 60.0;
    }

    public UserCardResponse toCardResponse(UserEntity entity) {
        return new UserCardResponse(
                entity.getId(),
                entity.getName(),
                entity.getSurname()
        );
    }

    public UserProfileResponse toProfileResponse(UserEntity user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getBackgroundImageUrl()
        );
    }
}
