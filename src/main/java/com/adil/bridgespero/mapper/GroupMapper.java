package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.adil.bridgespero.domain.model.constant.AppConstant.DATE_FORMAT;

@Component
public class GroupMapper {

    public GroupTeacherCardResponse toTeacherCardResponse(GroupEntity entity) {
        return GroupTeacherCardResponse.builder()
                .name(entity.getName())
                .status(entity.getStatus().toString().toLowerCase())
                .startDate(entity.getStartDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .numberOfStudents(entity.getStudents().size())
                .maxStudents(entity.getMaxStudents())
                .schedule(entity.getLessonSchedules().stream().map(this::mapSchedule).toList())
                .build();
    }

    private String mapSchedule(LessonScheduleEntity entity) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String day = entity.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String start = entity.getStartTime().format(timeFormatter);
        String end = entity.getEndTime().format(timeFormatter);

        return String.format("%s: %s-%s", day, start, end);
    }
}
