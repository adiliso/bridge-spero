package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.ScheduleEntity;
import com.adil.bridgespero.domain.model.dto.filter.GroupFilter;
import com.adil.bridgespero.domain.model.dto.request.GroupCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.GroupEditRequest;
import com.adil.bridgespero.domain.model.dto.request.ResourceCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupMembersResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.enums.GroupMemberStatus;
import com.adil.bridgespero.domain.model.enums.JoinRequestStatus;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.JoinRequestRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.GroupFullException;
import com.adil.bridgespero.exception.GroupNotFoundException;
import com.adil.bridgespero.exception.GroupWithZoomIdNotFoundException;
import com.adil.bridgespero.exception.ScheduleNotFoundException;
import com.adil.bridgespero.exception.SyllabusAlreadyExistsException;
import com.adil.bridgespero.exception.UserGroupAlreadyExistsException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ResourceMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.UserMapper;
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
    UserMapper userMapper;

    FileStorageService fileStorageService;
    UserService userService;
    TeacherService teacherService;
    MeetingService meetingService;
    CategoryService categoryService;
    UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;

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

    public void checkGroupExists(Long groupId) {
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

    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)" +
                  " or @securityService.isStudentOfGroup(#groupId)")
    public List<ResourceResponse> getResources(Long groupId, ResourceType type) {
        return resourceRepository.findAllByGroupIdAndType(groupId, type)
                .stream()
                .map(resourceMapper::toResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public ScheduleResponse createSchedule(Long groupId, ScheduleRequest request) {
        checkGroupExists(groupId);

        var entity = scheduleMapper.toEntity(groupId, request);

        ScheduleEntity saved = scheduleRepository.save(entity);
        return scheduleMapper.toScheduleResponse(saved);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfSchedule(#id)")
    public void updateSchedule(Long id, ScheduleRequest request) {
        ScheduleEntity entity = getSchedule(id);

        scheduleMapper.updateSchedule(id, entity, request);
    }

    private ScheduleEntity getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
    }

    public void checkScheduleExistsById(Long id) {
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
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public void deleteSyllabus(Long groupId) {
        var group = getById(groupId);

        fileStorageService.deleteFile(group.getSyllabus());

        group.setSyllabus(null);
    }

    private void checkSyllabusExists(GroupEntity group) {
        if (group.getSyllabus() != null && !group.getSyllabus().isEmpty()) {
            throw new SyllabusAlreadyExistsException(group.getId());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfSchedule(#id)")
    public void deleteSchedule(Long id) {
        checkScheduleExistsById(id);

        scheduleRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public Long createRecording(Long groupId, ResourceCreateRequest request) {
        String path = fileStorageService.saveFile(request.file(), RECORDING);

        var entity = resourceMapper.toEntity(groupId, path, RECORDING, request);

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

        String imageUrl = null;
        String syllabusUrl = null;
        if (request.image() != null && !request.image().isEmpty()) {
            imageUrl = fileStorageService.saveFile(request.image(), ResourceType.IMAGE);
        }
        if (request.syllabus() != null && !request.syllabus().isEmpty()) {
            syllabusUrl = fileStorageService.saveFile(request.syllabus(), ResourceType.SYLLABUS);
        }

        var group = groupMapper.toEntity(userId, imageUrl, syllabusUrl, request);
        var savedGroup = groupRepository.save(group);

        return savedGroup.getId();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public void edit(Long id, Long userId, GroupEditRequest request) {
        teacherService.checkTeacherExists(userId);
        categoryService.checkCategoryExists(request.categoryId());

        var group = getById(id);

        groupMapper.update(userService.getCurrentUserId(), group, request);

        if (request.image() != null && !request.image().isEmpty()) {
            String newImageUrl = fileStorageService.saveFile(request.image(), ResourceType.IMAGE);
            group.setImageUrl(newImageUrl);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public String startLesson(Long id, String email) {
        var group = getById(id);

        var zoomMeeting = meetingService.createMeeting(email, group);

        group.setStartUrl(zoomMeeting.getStartUrl());
        group.setJoinUrl(zoomMeeting.getJoinUrl());

        return zoomMeeting.getStartUrl();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)" +
                  " or @securityService.isStudentOfGroup(#id)")
    public String joinLesson(Long id) {
        var group = getById(id);
        return group.getJoinUrl();
    }


    @Transactional
    public void endLessonByZoomMeetingId(String meetingId) {
        var group = getByZoomMeetingId(meetingId);

        group.setMeetingId(null);
        group.setStartUrl(null);
        group.setJoinUrl(null);
        group.setMeetingActive(false);
    }

    private GroupEntity getByZoomMeetingId(String meetingId) {
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

    public GroupMembersResponse getAllMembers(Long id) {

        checkGroupExists(id);
        var userCards = userRepository.findAllByGroups_Id(id)
                .stream()
                .map(userMapper::toCardResponse).toList();

        return new GroupMembersResponse(
                userCards.size(),
                userCards
        );
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id) or" +
                  " @securityService.isCurrentUser(#studentId)")
    public void deleteMembership(Long id, Long studentId) {

        userService.checkUserExists(studentId);
        groupRepository.deleteMembership(id, studentId);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#id)")
    public void delete(Long id) {
        checkGroupExists(id);
        groupRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfGroup(#groupId)")
    public void addMember(Long groupId, Long studentId) {
        var group = getById(groupId);
        var user = userService.findById(studentId);
        checkUserGroupAlreadyExists(studentId, groupId);
        checkGroupIsFull(groupId, group.getMaxStudents());

        group.getUsers().add(user);
        user.getGroups().add(group);
    }

    public void checkGroupIsFull(Long groupId, int maxStudents) {
        if (groupRepository.countUsersInGroup(groupId) >= maxStudents)
            throw new GroupFullException(groupId);
    }

    public void checkUserGroupAlreadyExists(Long userId, Long groupId) {
        if (groupRepository.existsByIdAndUsers_Id(groupId, userId)) {
            throw new UserGroupAlreadyExistsException(userId, groupId);
        }
    }

    public GroupMemberStatus getMemberStatus(Long groupId, Long userId) {
        checkGroupExists(groupId);
        userService.checkUserExists(userId);

        if (groupRepository.existsByIdAndUsers_Id(groupId, userId)) return GroupMemberStatus.MEMBER;
        else if (joinRequestRepository
                .existsByStudent_IdAndGroup_IdAndStatus(userId, groupId, JoinRequestStatus.PENDING)) {
            return GroupMemberStatus.PENDING;
        }
        return GroupMemberStatus.NOT_MEMBER;
    }
}
