package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDetailedCardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    public TeacherCardResponse toCardResponse(TeacherDetailEntity entity) {
        return new TeacherCardResponse(
                entity.getId(),
                entity.getUser().getProfilePictureUrl(),
                entity.getUser().getName(),
                entity.getUser().getSurname(),
                entity.getRating(),
                getActiveStudents(entity)
        );
    }

    public TeacherDetailedCardResponse toDetailedCardResponse(TeacherDetailEntity entity) {
        return new TeacherDetailedCardResponse(
                entity.getId(),
                entity.getUser().getProfilePictureUrl(),
                entity.getUser().getName(),
                entity.getUser().getSurname(),
                entity.getRating(),
                getActiveStudents(entity),
                entity.getExperience().getDescription()
        );
    }

    public TeacherDashboardResponse toDashboardResponse(TeacherDetailEntity entity, List<GroupTeacherDashboardResponse> groups) {
        return TeacherDashboardResponse.builder()
                .name(entity.getUser().getName())
                .activeGroups(getActiveGroups(entity))
                .activeStudents(getActiveStudents(entity))
                .totalEarning(null)
                .rating(entity.getRating())
                .groups(groups)
                .build();
    }

    private Integer getActiveGroups(TeacherDetailEntity entity) {
        return entity.getCreatedGroups()
                .stream()
                .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                .toList()
                .size();
    }

    private Integer getActiveStudents(TeacherDetailEntity entity) {
        return entity.getCreatedGroups()
                .stream()
                .filter(group -> group.getStatus().equals(GroupStatus.ACTIVE))
                .mapToInt(group -> group.getUsers().size())
                .sum();
    }
}
