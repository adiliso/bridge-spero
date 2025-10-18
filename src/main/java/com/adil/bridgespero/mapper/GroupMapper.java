package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupScheduleCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.adil.bridgespero.domain.model.constant.AppConstant.DATE_FORMAT;

@Component
public class GroupMapper {

    public GroupTeacherCardResponse toGroupTeacherCardResponse(GroupEntity entity) {
        return GroupTeacherCardResponse.builder()
                .name(entity.getName())
                .status(entity.getStatus().toString().toLowerCase())
                .price(entity.getPrice())
                .startDate(entity.getStartDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .numberOfStudents(entity.getStudents().size())
                .maxStudents(entity.getMaxStudents())
                .minStudents(entity.getMinStudents())
                .schedule(entity.getLessonSchedules().stream().map(this::mapSchedule).toList())
                .build();
    }

    public GroupScheduleCardResponse toGroupScheduleCardResponse(GroupEntity entity, DayOfWeek day) {
        return new GroupScheduleCardResponse(
                entity.getName(),
                entity.getLessonSchedules()
                        .stream()
                        .filter(sc -> sc.getDayOfWeek().equals(day))
                        .map(this::mapTime)
                        .toList());
    }

    private String mapSchedule(LessonScheduleEntity entity) {
        String day = entity.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        return String.format("%s: %s", day, mapTime(entity));
    }

    private String mapTime(LessonScheduleEntity entity) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = entity.getStartTime().format(timeFormatter);
        String end = entity.getEndTime().format(timeFormatter);
        return String.format("%s-%s", start, end);
    }
}
