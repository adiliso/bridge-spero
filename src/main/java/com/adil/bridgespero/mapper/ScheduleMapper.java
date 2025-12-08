package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.ScheduleEntity;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.response.ScheduleDayResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleHourCardResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleTeacherEventResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleUserEventResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.adil.bridgespero.constant.AppConstant.GROUP_DATE_FORMATTER;
import static com.adil.bridgespero.constant.AppConstant.TIME_FORMATTER;
import static java.time.format.TextStyle.FULL;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    public ScheduleWeekResponse toScheduleWeekResponse(LocalDate startOfWeek,
                                                       LocalDate endOfWeek,
                                                       List<ScheduleEntity> scheduleEntities) {

        Map<DayOfWeek, List<ScheduleEntity>> daySchedule = scheduleEntities.stream()
                .map(s -> Map.entry(s.getDayOfWeek(), s))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return new ScheduleWeekResponse(
                String.format("%s - %s", startOfWeek.format(GROUP_DATE_FORMATTER), endOfWeek.format(GROUP_DATE_FORMATTER)),
                daySchedule.keySet().stream()
                        .map(k -> toScheduleDayResponse(k, daySchedule.get(k)))
                        .toList()
        );
    }

    private ScheduleDayResponse toScheduleDayResponse(DayOfWeek dayOfWeek, List<ScheduleEntity> schedules) {
        return new ScheduleDayResponse(
                dayOfWeek.getDisplayName(FULL, Locale.getDefault()),
                schedules
                        .stream()
                        .map(this::toScheduleHourCardResponse)
                        .toList()
        );
    }

    public ScheduleHourCardResponse toScheduleHourCardResponse(ScheduleEntity entity) {
        var group = entity.getGroup();
        return new ScheduleHourCardResponse(
                entity.getId(),
                group.getName(),
                entity.getStartTime().format(TIME_FORMATTER));
    }

    public ScheduleResponse toScheduleResponse(ScheduleEntity entity) {
        return new ScheduleResponse(
                entity.getId(),
                entity.getDayOfWeek().getDisplayName(FULL, Locale.getDefault()),
                entity.getStartTime().format(TIME_FORMATTER),
                entity.getEndTime().format(TIME_FORMATTER)
        );
    }

    public ScheduleEntity toEntity(Long groupId, ScheduleRequest request) {
        GroupEntity group = GroupEntity.builder()
                .id(groupId)
                .build();

        return ScheduleEntity.builder()
                .dayOfWeek(DayOfWeek.valueOf(request.dayOfWeek().toUpperCase()))
                .startTime(LocalTime.parse(request.startTime(), TIME_FORMATTER))
                .endTime(LocalTime.parse(request.endTime(), TIME_FORMATTER))
                .group(group)
                .build();
    }

    public void updateSchedule(Long id, ScheduleEntity entity, ScheduleRequest request) {
        if (id == null && request == null) {
            return;
        }

        var requestEntity = toEntity(id, request);

        if (requestEntity.getDayOfWeek() != null) {
            entity.setDayOfWeek(requestEntity.getDayOfWeek());
        }
        if (requestEntity.getStartTime() != null) {
            entity.setStartTime(requestEntity.getStartTime());
        }
        if (requestEntity.getEndTime() != null) {
            entity.setEndTime(requestEntity.getEndTime());
        }
    }

    public ScheduleTeacherEventResponse toScheduleTeacherEventResponse(ScheduleEntity entity) {
        var group = entity.getGroup();
        return ScheduleTeacherEventResponse.builder()
                .id(entity.getId())
                .groupId(group.getId())
                .name(group.getName())
                .status(group.getStatus().toString().toLowerCase())
                .startTime(entity.getStartTime().format(TIME_FORMATTER))
                .numberOfStudents(group.getUsers().size())
                .maxStudents(group.getMaxStudents())
                .startUrl(group.getStartUrl())
                .isMeetingActive(group.isMeetingActive())
                .build();
    }

    public ScheduleUserEventResponse toScheduleUserEventResponse(ScheduleEntity entity) {
        var group = entity.getGroup();
        return new ScheduleUserEventResponse(
                entity.getId(),
                group.getId(),
                group.getTeacher().getId(),
                group.getName(),
                group.getStatus().toString().toLowerCase(),
                entity.getStartTime().format(TIME_FORMATTER),
                getTeacherNameSurname(group),
                group.getJoinUrl(),
                group.isMeetingActive()
        );
    }

    private String getTeacherNameSurname(GroupEntity entity) {
        return String.format("%s %s", entity.getTeacher().getUser().getName(),
                entity.getTeacher().getUser().getSurname());
    }
}
