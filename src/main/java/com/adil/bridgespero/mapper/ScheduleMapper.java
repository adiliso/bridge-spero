package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.response.ScheduleDayResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
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

    private final GroupMapper groupMapper;

    public ScheduleWeekResponse toScheduleWeekResponse(LocalDate startOfWeek,
                                                       LocalDate endOfWeek,
                                                       List<GroupEntity> groups) {
        Map<DayOfWeek, List<GroupEntity>> scheduledGroups = groups.stream()
                .flatMap(g -> g.getLessonSchedules().stream()
                        .map(ls -> Map.entry(ls.getDayOfWeek(), g))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return new ScheduleWeekResponse(
                String.format("%s - %s", startOfWeek.format(GROUP_DATE_FORMATTER), endOfWeek.format(GROUP_DATE_FORMATTER)),
                scheduledGroups.keySet().stream()
                        .map(k -> toScheduleDayResponse(k, scheduledGroups.get(k)))
                        .toList()
        );
    }

    private ScheduleDayResponse toScheduleDayResponse(DayOfWeek dayOfWeek, List<GroupEntity> groups) {
        return new ScheduleDayResponse(
                dayOfWeek.getDisplayName(FULL, Locale.getDefault()),
                groups
                        .stream()
                        .map(g -> groupMapper.toGroupScheduleCardResponse(g, dayOfWeek))
                        .toList()
        );
    }

    public ScheduleResponse toScheduleResponse(LessonScheduleEntity entity) {
        return new ScheduleResponse(
                entity.getId(),
                entity.getDayOfWeek().getDisplayName(FULL, Locale.getDefault()),
                entity.getStartTime().format(TIME_FORMATTER),
                entity.getEndTime().format(TIME_FORMATTER)
        );
    }

    public LessonScheduleEntity toEntity(Long groupId, ScheduleRequest request) {
        GroupEntity group = GroupEntity.builder()
                .id(groupId)
                .build();

        return LessonScheduleEntity.builder()
                .dayOfWeek(DayOfWeek.valueOf(request.dayOfWeek().toUpperCase()))
                .startTime(LocalTime.parse(request.startTime(), TIME_FORMATTER))
                .endTime(LocalTime.parse(request.endTime(), TIME_FORMATTER))
                .group(group)
                .build();
    }

    public void updateSchedule(Long id, LessonScheduleEntity entity, ScheduleRequest request) {
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
}
