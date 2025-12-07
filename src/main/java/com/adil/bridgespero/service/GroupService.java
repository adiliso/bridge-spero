package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.GroupFilter;
import com.adil.bridgespero.domain.model.dto.request.GroupCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.ResourceCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.exception.GroupNotFoundException;
import com.adil.bridgespero.exception.GroupWithZoomIdNotFoundException;
import com.adil.bridgespero.exception.ScheduleNotFoundException;
import com.adil.bridgespero.exception.SyllabusAlreadyExistsException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ResourceMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
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

import java.util.List;

import static com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE;
import static com.adil.bridgespero.domain.model.enums.ResourceType.RECORDING;
import static com.adil.bridgespero.domain.model.enums.ResourceType.RESOURCES;
import static com.adil.bridgespero.domain.model.enums.ResourceType.SYLLABUS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupService {

    GroupRepository groupRepository;
    ScheduleRepository scheduleRepository;
    ResourceRepository resourceRepository;

    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;
    ResourceMapper resourceMapper;

    FileStorageService fileStorageService;
    UserService userService;
    TeacherService teacherService;
    ZoomService zoomService;
    CategoryService categoryService;

    public PageResponse<GroupCardResponse> getTopRated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("teacher.rating").descending());

        var responses = groupRepository.findAllByStatus(ACTIVE, pageable)
                .map(groupMapper::toCardResponse);

        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages());
    }

    public GroupDetailsResponse getDetailsById(Long groupId) {
        return groupMapper.toGroupDetailsResponse(getById(groupId));
    }

    private void checkGroupExists(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException(groupId);
        }
    }

    public GroupEntity getById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    public List<ScheduleResponse> getScheduleByGroupId(Long groupId) {
        checkGroupExists(groupId);
        return scheduleRepository.findAllByGroupId(groupId)
                .stream()
                .map(scheduleMapper::toScheduleResponse)
                .toList();
    }

    public String getSyllabus(Long id) {
        return getById(id).getSyllabus();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)" +
                  " or @securityService.isStudentOfGroup(#id)")
    public List<ResourceResponse> getResources(Long id, ResourceType type) {
        checkGroupExists(id);
        return resourceRepository.findAllByGroupIdAndType(id, type)
                .stream()
                .map(resourceMapper::toResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public ScheduleResponse createSchedule(Long groupId, ScheduleRequest request) {
        checkGroupExists(groupId);

        var entity = scheduleMapper.toEntity(groupId, request);

        LessonScheduleEntity saved = scheduleRepository.save(entity);
        return scheduleMapper.toScheduleResponse(saved);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public void updateSchedule(Long id, ScheduleRequest request) {
        checkScheduleExistsById(id);
        LessonScheduleEntity entity = getSchedule(id);

        scheduleMapper.updateSchedule(id, entity, request);
    }

    private LessonScheduleEntity getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
    }

    private void checkScheduleExistsById(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException(id);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public String createSyllabus(Long groupId, SyllabusCreateRequest request) {

        var group = getById(groupId);
        checkSyllabusExists(group);

        String path = fileStorageService.saveFile(request.file(), SYLLABUS);

        group.setSyllabus(path);

        return path;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public void deleteSyllabus(Long id) {
        var group = getById(id);

        deleteFile(group.getSyllabus());

        group.setSyllabus(null);
    }

    private void checkSyllabusExists(GroupEntity group) {
        if (group.getSyllabus() != null && !group.getSyllabus().isEmpty()) {
            throw new SyllabusAlreadyExistsException(group.getId());
        }
    }

    private void deleteFile(String filePath) {
        fileStorageService.deleteFile(filePath);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public void deleteSchedule(Long id) {
        checkScheduleExistsById(id);

        scheduleRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public Long createRecording(Long id, ResourceCreateRequest request) {
        checkGroupExists(id);

        String path = fileStorageService.saveFile(request.file(), RECORDING);

        var entity = resourceMapper.toEntity(id, path, RECORDING, request);

        resourceRepository.save(entity);
        return entity.getId();
    }

    public PageResponse<GroupCardResponse> search(GroupFilter filter, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("teacher.rating"));
        var responses = groupRepository.findAll(SpecificationUtils.getGroupSpecification(filter), pageable)
                .map(groupMapper::toCardResponse);

        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages()
        );
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public Long create(Long userId, GroupCreateRequest request) {
        teacherService.checkTeacherExists(userId);
        categoryService.checkCategoryExists(request.categoryId());

        String imageUrl = fileStorageService.saveFile(request.image(), ResourceType.IMAGE);

        var group = groupMapper.toEntity(userId, imageUrl, request);
        var savedGroup = groupRepository.save(group);

        return savedGroup.getId();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public String startLesson(Long id, String email) {
        teacherService.checkTeacherExists(id);
        var group = getById(id);

        var zoomMeeting = zoomService.createMeeting(email, group);

        group.setStartUrl(zoomMeeting.getStartUrl());
        group.setJoinUrl(zoomMeeting.getJoinUrl());
        group.setMeetingActive(true);

        return zoomMeeting.getStartUrl();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)" +
                  " or @securityService.isStudentOfGroup(#id)")
    public String joinLesson(Long id) {
        var group = getById(id);
        return group.getJoinUrl();
    }


    @Transactional
    public void endLessonByZoomMeetingId(Long meetingId) {
        var group = getByZoomMeetingId(meetingId);

        group.setMeetingId(null);
        group.setStartUrl(null);
        group.setJoinUrl(null);
        group.setMeetingActive(false);
    }

    private GroupEntity getByZoomMeetingId(Long meetingId) {
        return groupRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new GroupWithZoomIdNotFoundException(meetingId));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public Long createResource(Long id, ResourceCreateRequest request) {
        checkGroupExists(id);

        String path = fileStorageService.saveFile(request.file(), RESOURCES);

        var entity = resourceMapper.toEntity(id, path, RESOURCES, request);

        resourceRepository.save(entity);
        return entity.getId();
    }

    @Transactional
    public void addStudent(Long groupId, Long userId) {
        checkGroupExists(groupId);
        var group = groupRepository.getReferenceById(groupId);
        userService.checkUserExists(userId);
        var user = userService.findById(userId);

        group.getUsers().add(user);
        user.getGroups().add(group);
    }
}
