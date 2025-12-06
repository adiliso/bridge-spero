package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.model.dto.TeacherDto;
import com.adil.bridgespero.domain.model.dto.TeacherFilter;
import com.adil.bridgespero.domain.model.dto.response.GroupTeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.TeacherRepository;
import com.adil.bridgespero.exception.TeacherNotFoundException;
import com.adil.bridgespero.exception.UserNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.TeacherMapper;
import com.adil.bridgespero.util.SpecificationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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
    FileStorageService fileStorageService;

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

    @PreAuthorize("hasRole('TEACHER')")
    public TeacherDashboardResponse getDashboard(Long id) {
        return teacherMapper.toDashboardResponse(teacherRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id)),
                groupRepository.findAllByTeacherIdAndStatusAndDayOfWeek(id, DayOfWeek.from(LocalDate.now()))
                        .stream()
                        .filter(group -> GroupStatus.ACTIVE.equals(group.getStatus()))
                        .map(groupMapper::toGroupTeacherDashboardResponse)
                        .toList());
    }

    @PreAuthorize("hasRole('TEACHER')")
    public List<GroupTeacherDashboardResponse> getGroups(Long id, GroupStatus status) {
        return groupRepository.findAllByTeacherIdAndStatus(id, status)
                .stream()
                .map(groupMapper::toGroupTeacherDashboardResponse)
                .toList();
    }

    @PreAuthorize("hasRole('TEACHER')")
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

    public PageResponse<TeacherCardResponse> search(TeacherFilter filter, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("rating").descending());

        var responses = teacherRepository.findAll(SpecificationUtils.getTeacherSpecification(filter), pageable)
                .map(teacherMapper::toCardResponse);

        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages()
        );
    }

    public void checkTeacherExists(Long userId) {
        if (!teacherRepository.existsById(userId)) {
            throw new TeacherNotFoundException(userId);
        }
    }

    @Transactional
    public void save(TeacherDto teacherDto) {
        var entity = teacherMapper.toEntity(teacherDto);
        teacherRepository.save(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public String updateDemoVideo(MultipartFile demoVideo, Long userId) {
        var entity = getById(userId);

        fileStorageService.deleteFile(entity.getDemoVideoUrl());

        String path = fileStorageService.saveFile(demoVideo, ResourceType.DEMO_VIDEO);
        entity.setDemoVideoUrl(path);
        teacherRepository.save(entity);
        return path;
    }

    private TeacherDetailEntity getById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFoundException(id));
    }
}
