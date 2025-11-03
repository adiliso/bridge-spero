package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.mapper.ScheduleMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    GroupRepository groupRepository;
    ScheduleMapper scheduleMapper;

    public ScheduleWeekResponse getSchedule(Long id) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        var weekGroups = groupRepository.findAllByUserIdAndSchedule(id, startOfWeek, endOfWeek);
        return scheduleMapper.toScheduleWeekResponse(startOfWeek, endOfWeek, weekGroups);
    }
}
