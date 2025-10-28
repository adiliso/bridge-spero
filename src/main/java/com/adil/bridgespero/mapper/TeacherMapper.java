package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.TeacherEntity;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public TeacherDashboardResponse toDashboardResponse(TeacherEntity entity, List<GroupEntity> groups) {
        return TeacherDashboardResponse.builder()
                .name(entity.getName())
                .activeGroups(getActiveGroups(entity))
                .activeStudents(getActiveStudents(entity))
                .totalEarning(null)
                .rating(entity.getRating())
                .groups(groups
                        .stream()
                        .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                        .map(groupMapper::toGroupTeacherCardResponse)
                        .toList())
                .build();
    }

    private Integer getActiveGroups(TeacherEntity entity) {
        return entity.getGroups()
                .stream()
                .filter(group -> group.getStatus().equals(GroupStatus.ACTIVE))
                .toList()
                .size();
    }

    private Integer getActiveStudents(TeacherEntity entity) {
        return entity.getGroups()
                .stream()
                .filter(group -> group.getStatus().equals(GroupStatus.ACTIVE))
                .mapToInt(group -> group.getStudents().size())
                .sum();
    }
}
