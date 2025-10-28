package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupScheduleCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class GroupMapper {

    public GroupCardResponse toCardResponse(GroupEntity entity) {
        return new GroupCardResponse(
                entity.getId(),
                entity.getTeacher().getId(),
                entity.getImageUrl(),
                entity.getSubjectCategory().toString(),
                entity.getLanguage().getValue(),
                entity.getName(),
                entity.getTeacher().getName(),
                entity.getTeacher().getSurname(),
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
}
