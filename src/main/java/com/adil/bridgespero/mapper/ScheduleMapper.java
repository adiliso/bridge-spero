package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.ScheduleDayResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.adil.bridgespero.domain.model.constant.AppConstant.DATE_FORMAT;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    private final GroupMapper groupMapper;

    public ScheduleResponse toScheduleResponse(LocalDate startOfWeek, LocalDate endOfWeek, List<GroupEntity> groups) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Map<DayOfWeek, List<GroupEntity>> scheduledGroups = groups.stream()
                .flatMap(g -> g.getLessonSchedules().stream()
                        .map(ls -> Map.entry(ls.getDayOfWeek(), g))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return new ScheduleResponse(
                String.format("%s - %s", startOfWeek.format(formatter), endOfWeek.format(formatter)),
                scheduledGroups.keySet().stream()
                        .map(k -> toScheduleDayResponse(k, scheduledGroups.get(k)))
                        .toList()
        );
    }

    private ScheduleDayResponse toScheduleDayResponse(DayOfWeek dayOfWeek, List<GroupEntity> groups) {
        return new ScheduleDayResponse(
                dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                groups
                        .stream()
                        .map(g -> groupMapper.toGroupScheduleCardResponse(g, dayOfWeek))
                        .toList()
        );
    }
}
