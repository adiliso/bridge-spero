package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.TeacherEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    public TeacherCardResponse toCardResponse(TeacherEntity entity) {
        return TeacherCardResponse.builder()
                .id(entity.getId())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .name(entity.getUser().getName())
                .surname(entity.getUser().getSurname())
                .rating(entity.getRating())
                .activeStudents(getActiveStudents(entity))
                .experience(entity.getExperience().getDescription())
                .build();
    }

    public TeacherDashboardResponse toDashboardResponse(TeacherEntity entity, List<GroupTeacherCardResponse> groups) {
        return TeacherDashboardResponse.builder()
                .name(entity.getUser().getName())
                .activeGroups(getActiveGroups(entity))
                .activeStudents(getActiveStudents(entity))
                .totalEarning(null)
                .rating(entity.getRating())
                .groups(groups)
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
