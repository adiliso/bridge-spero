package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.TeacherEntity;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    private final GroupMapper groupMapper;

    public TeacherCardResponse toCardResponse(TeacherEntity entity) {
        return TeacherCardResponse.builder()
                .id(entity.getId())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .name(entity.getName())
                .surname(entity.getSurname())
                .rating(entity.getRating())
                .activeStudents(getActiveStudents(entity))
                .subjects(entity.getSubjects().stream().toList())
                .experience(entity.getExperience().getDescription())
                .build();
    }

    public TeacherDashboardResponse toDashboardResponse(TeacherEntity entity) {
        return TeacherDashboardResponse.builder()
                .name(entity.getName())
                .activeStudents(getActiveStudents(entity))
                .totalEarning(null)
                .rating(entity.getRating())
                .groups(entity.getGroups()
                        .stream()
                        .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                        .map(groupMapper::toTeacherCardResponse)
                        .toList())
                .build();
    }

    private Integer getActiveStudents(TeacherEntity entity) {
        return entity.getGroups()
                .stream()
                .filter(group -> group.getStatus().equals(GroupStatus.ACTIVE))
                .mapToInt(group -> group.getStudents().size())
                .sum();
    }
}
