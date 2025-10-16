package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.TeacherEntity;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public TeacherCardResponse toCardResponse(TeacherEntity entity) {
        return TeacherCardResponse.builder()
                .id(entity.getId())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .name(entity.getName())
                .surname(entity.getSurname())
                .rating(entity.getRating())
                .activeStudents(entity.getGroups()
                        .stream()
                        .filter(group -> group.getGroupStatus().equals(GroupStatus.ACTIVE))
                        .mapToInt(group -> group.getStudents().size())
                        .sum()
                )
                .subjects(entity.getSubjects().stream().toList())
                .experience(entity.getExperience().getDescription())
                .build();
    }
}
