package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.TeacherRepository;
import com.adil.bridgespero.exception.TeacherNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.TeacherMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherService {

    TeacherRepository teacherRepository;
    GroupRepository groupRepository;
    TeacherMapper teacherMapper;
    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;

    public PageResponse<TeacherCardResponse> getTopRated(final int pageNumber, final int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("rating").descending());

        var responses = teacherRepository.findAll(pageable)
                .map(teacherMapper::toCardResponse);
        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages());
    }

    public TeacherDashboardResponse getDashboard(Long id) {
        return teacherMapper.toDashboardResponse(teacherRepository.findById(id)
                        .orElseThrow(() -> new TeacherNotFoundException(id)),
                groupRepository.findAllByTeacherIdAndStatusAndDayOfWeek(id, DayOfWeek.from(LocalDate.now()))
                        .stream()
                        .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                        .map(groupMapper::toGroupTeacherCardResponse)
                        .toList());
    }

    public List<GroupTeacherCardResponse> getGroups(Long id, int parameter) {
        List<GroupEntity> entities = switch (parameter) {
            case 0 -> groupRepository.findAllByTeacherIdAndStatus(id, ACTIVE);
            case 1 -> groupRepository.findAllByTeacherIdAndStatus(id, GroupStatus.PENDING);
            case 2 -> groupRepository.findAllByTeacherIdAndStatus(id, GroupStatus.COMPLETED);
            case 3 -> groupRepository.findAllByTeacherIdAndStatus(id, GroupStatus.DRAFT);
            default -> new ArrayList<>();
        };
        return entities
                .stream()
                .map(groupMapper::toGroupTeacherCardResponse)
                .toList();
    }

    public ScheduleWeekResponse getSchedule(Long id) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        return scheduleMapper.toScheduleWeekResponse(
                startOfWeek,
                endOfWeek,
                groupRepository.findAllByTeacherIdAndSchedule(
                        id,
                        startOfWeek,
                        endOfWeek
                ));
    }
}
