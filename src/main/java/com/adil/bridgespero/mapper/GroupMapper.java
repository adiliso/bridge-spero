package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.CategoryEntity;
import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.model.dto.request.GroupCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupScheduleCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupUserDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.adil.bridgespero.constant.AppConstant.GROUP_DATE_FORMATTER;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final TeacherMapper teacherMapper;

    public GroupCardResponse toCardResponse(GroupEntity entity) {
        return new GroupCardResponse(
                entity.getId(),
                entity.getTeacher().getId(),
                entity.getImageUrl(),
                entity.getCategory().getName(),
                entity.getLanguage().getValue(),
                entity.getName(),
                entity.getTeacher().getUser().getName(),
                entity.getTeacher().getUser().getSurname(),
                entity.getPrice()
        );
    }

    public GroupTeacherDashboardResponse toGroupTeacherDashboardResponse(GroupEntity entity) {
        return GroupTeacherDashboardResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus().toString().toLowerCase())
                .startTime(getStartTime(entity))
                .numberOfStudents(entity.getUsers().size())
                .maxStudents(entity.getMaxStudents())
                .minStudents(entity.getMinStudents())
                .startUrl(entity.getStartUrl())
                .isMeetingActive(entity.isMeetingActive())
                .build();
    }

    public GroupUserDashboardResponse toGroupUserDashboardResponse(GroupEntity entity) {
        return new GroupUserDashboardResponse(
                entity.getId(),
                entity.getTeacher().getId(),
                entity.getName(),
                entity.getStatus().toString().toLowerCase(),
                getStartTime(entity),
                getTeacherNameSurname(entity),
                entity.getJoinUrl(),
                entity.isMeetingActive()
        );
    }

    private String getTeacherNameSurname(GroupEntity entity) {
        return String.format("%s %s", entity.getTeacher().getUser().getName(),
                entity.getTeacher().getUser().getSurname());
    }

    private String getStartTime(GroupEntity entity) {
        return entity.getLessonSchedules()
                .stream()
                .filter(sc -> sc.getDayOfWeek().equals(DayOfWeek.from(LocalDate.now())))
                .map(this::mapTime)
                .toString();
    }

    public GroupScheduleCardResponse toGroupScheduleCardResponse(GroupEntity entity, DayOfWeek day) {
        return new GroupScheduleCardResponse(
                entity.getId(),
                entity.getName(),
                entity.getLessonSchedules()
                        .stream()
                        .filter(sc -> sc.getDayOfWeek().equals(day))
                        .map(this::mapTime)
                        .toString());
    }

    private String mapTime(LessonScheduleEntity entity) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = entity.getStartTime().format(timeFormatter);
        String end = entity.getEndTime().format(timeFormatter);
        return String.format("%s-%s", start, end);
    }

    public GroupDetailsResponse toGroupDetailsResponse(GroupEntity entity) {
        return new GroupDetailsResponse(
                entity.getName(),
                entity.getCategory().getName(),
                entity.getLanguage().getValue(),
                entity.getDescription(),
                ChronoUnit.WEEKS.between(entity.getStartDate(), entity.getEndDate()),
                entity.getUsers().size(),
                entity.getMaxStudents(),
                entity.getStartDate().format(GROUP_DATE_FORMATTER),
                entity.getPrice(),
                teacherMapper.toDetailedCardResponse(entity.getTeacher())
        );
    }

    public GroupEntity toEntity(Long userId, String imageUrl, String syllabus, GroupCreateRequest request) {
        var teacher = TeacherDetailEntity.builder()
                .id(userId)
                .build();
        var category = CategoryEntity.builder()
                .id(request.categoryId())
                .build();

        LocalDate startDate = LocalDate.parse(request.startDate(), GROUP_DATE_FORMATTER);
        LocalDate endDate = null;
        if (request.durationInMonths() != null) endDate = startDate.plusMonths(request.durationInMonths());

        return GroupEntity.builder()
                .createdBy(userId)
                .teacher(teacher)
                .name(request.name())
                .category(category)
                .language(request.language())
                .startDate(startDate)
                .endDate(endDate)
                .status(GroupStatus.ACTIVE)
                .maxStudents(request.maxStudents())
                .minStudents(request.minStudents())
                .price(request.price())
                .description(request.description())
                .imageUrl(imageUrl)
                .syllabus(syllabus)
                .build();
    }
}
