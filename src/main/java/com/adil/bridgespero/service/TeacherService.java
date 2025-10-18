package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.TeacherRepository;
import com.adil.bridgespero.exception.TeacherNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.TeacherMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.adil.bridgespero.domain.model.constant.AppConstant.SCHEDULE_DATE_FORMAT;
import static com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE;
import static com.adil.bridgespero.domain.model.enums.GroupStatus.COMPLETED;
import static com.adil.bridgespero.domain.model.enums.GroupStatus.PENDING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherService {

    TeacherRepository teacherRepository;
    GroupRepository groupRepository;
    TeacherMapper teacherMapper;
    GroupMapper groupMapper;

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
                .orElseThrow(() -> new TeacherNotFoundException(id)));
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

    public ScheduleResponse getTodaySchedule(Long id, int parameter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SCHEDULE_DATE_FORMAT);
        LocalDate date = LocalDate.now().plusDays(parameter);
        return new ScheduleResponse(
                date.format(formatter),
                groupRepository.findAllByTeacherIdAndStatusInAndDateBetweenStartAndEnd(
                                id,
                                List.of(ACTIVE, PENDING, COMPLETED),
                                LocalDate.now())
                        .stream()
                        .map(g -> groupMapper.toGroupScheduleCardResponse(g, date.getDayOfWeek()))
                        .toList()
        );
    }
}
