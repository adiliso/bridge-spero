package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.TeacherDto;
import com.adil.bridgespero.domain.model.dto.request.TeacherUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.ScheduleTeacherEventResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDetailedCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherProfileResponse;
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
                getActiveStudents(entity)
        );
    }

    public TeacherDashboardResponse toDashboardResponse(TeacherDetailEntity entity, List<ScheduleTeacherEventResponse> groups) {
        return TeacherDashboardResponse.builder()
                .name(entity.getUser().getName())
                .activeGroups(getActiveGroups(entity))
                .activeStudents(getActiveStudents(entity))
                .totalEarning(null)
                .rating(entity.getRating())
                .events(groups)
                .build();
    }

    public TeacherDetailEntity toEntity(TeacherDto dto) {
        if (dto == null) return null;

        UserEntity user = UserEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .enabled(dto.getEnabled())
                .agreedToTerms(dto.getAgreedToTerms())
                .bio(dto.getBio())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .build();

        return TeacherDetailEntity.builder()
                .id(dto.getId())
                .user(user)
                .demoVideoUrl(dto.getDemoVideoUrl())
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

    public TeacherProfileResponse toProfileResponse(TeacherDetailEntity teacherDetail) {
        var user = teacherDetail.getUser();
        return new TeacherProfileResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                teacherDetail.getRating(),
                getActiveStudents(teacherDetail),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getBackgroundImageUrl(),
                teacherDetail.getDemoVideoUrl()
        );
    }

    public void update(UserEntity user, TeacherUpdateRequest request) {
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setBio(request.getBio());
        user.setPhone(request.getPhoneCode() + request.getPhoneNumber());
    }
}
