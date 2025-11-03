package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.entity.RecordingEntity;
import com.adil.bridgespero.domain.entity.ResourceEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupScheduleCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.adil.bridgespero.domain.model.constant.AppConstant.GROUP_DATE_FORMATTER;
import static com.adil.bridgespero.domain.model.constant.AppConstant.RECORDING_DATE_FORMATTER;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final TeacherMapper teacherMapper;

    public GroupCardResponse toCardResponse(GroupEntity entity) {
        return new GroupCardResponse(
                entity.getId(),
                entity.getTeacher().getId(),
                entity.getImageUrl(),
                entity.getSubjectCategory().toString(),
                entity.getLanguage().getValue(),
                entity.getName(),
                entity.getTeacher().getUser().getName(),
                entity.getTeacher().getUser().getSurname(),
                entity.getPrice()
        );
    }

    public GroupTeacherCardResponse toGroupTeacherCardResponse(GroupEntity entity) {
        return GroupTeacherCardResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus().toString().toLowerCase())
                .startDateTime(entity.getLessonSchedules()
                        .stream()
                        .filter(sc -> sc.getDayOfWeek().equals(DayOfWeek.from(LocalDate.now())))
                        .map(this::mapTime)
                        .toString())
                .numberOfStudents(entity.getStudents().size())
                .maxStudents(entity.getMaxStudents())
                .minStudents(entity.getMinStudents())
                .build();
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
                entity.getSubjectCategory().getName(),
                entity.getLanguage().getValue(),
                entity.getDescription(),
                ChronoUnit.WEEKS.between(entity.getStartDate(), entity.getEndDate()),
                entity.getStudents().size(),
                entity.getMaxStudents(),
                entity.getStartDate().format(GROUP_DATE_FORMATTER),
                entity.getPrice(),
                teacherMapper.toCardResponse(entity.getTeacher())
        );
    }

    public RecordingResponse toRecordingResponse(RecordingEntity entity) {
        return new RecordingResponse(
                entity.getUuid().toString(),
                entity.getDate().format(RECORDING_DATE_FORMATTER)
        );
    }

    public ResourceResponse toResourceResponse(ResourceEntity entity) {
        return new ResourceResponse(
                entity.getUuid().toString(),
                entity.getName()
        );
    }
}
