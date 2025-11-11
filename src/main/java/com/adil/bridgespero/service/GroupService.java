package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.entity.RecordingEntity;
import com.adil.bridgespero.domain.model.dto.GroupFilter;
import com.adil.bridgespero.domain.model.dto.request.GroupCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.RecordingCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.RecordingRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.exception.BaseException;
import com.adil.bridgespero.exception.GroupNotFoundException;
import com.adil.bridgespero.exception.ScheduleNotFoundException;
import com.adil.bridgespero.exception.SyllabusAlreadyExistsException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.util.GroupSpecificationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.INTERNAL_ERROR;
import static com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE;
import static com.adil.bridgespero.domain.model.enums.ResourceType.RECORDING;
import static com.adil.bridgespero.domain.model.enums.ResourceType.SYLLABUS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupService {

    GroupRepository groupRepository;
    RecordingRepository recordingRepository;
    ScheduleRepository scheduleRepository;
    ResourceRepository resourceRepository;

    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;

    FileStorageService fileStorageService;
    TeacherService teacherService;

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

    public List<RecordingResponse> getRecordings(Long id) {
        checkGroupExists(id);
        return recordingRepository.findAllByGroupId(id)
                .stream()
                .map(groupMapper::toRecordingResponse)
                .toList();
    }

    public List<ResourceResponse> getResources(Long id) {
        checkGroupExists(id);
        return resourceRepository.findAllByGroupId(id)
                .stream()
                .map(groupMapper::toResourceResponse)
                .toList();
    }

    @Transactional
    public ScheduleResponse createSchedule(Long groupId, ScheduleRequest request) {
        checkGroupExists(groupId);

        var entity = scheduleMapper.toEntity(groupId, request);

        LessonScheduleEntity saved = scheduleRepository.save(entity);
        return scheduleMapper.toScheduleResponse(saved);
    }

    @Transactional
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
    public String createSyllabus(Long groupId, SyllabusCreateRequest request) {

        var group = getById(groupId);
        checkSyllabusExists(group);

        String fileName = saveFile(request.file(), SYLLABUS);

        group.setSyllabus(fileName);

        return fileName;
    }

    @Transactional
    public void deleteSyllabus(Long id) {
        var group = getById(id);

        deleteFile(group.getSyllabus());

        group.setSyllabus(null);
    }

    private String saveFile(MultipartFile file, ResourceType resourceType) {

        String fileName;
        try {
            fileName = fileStorageService.saveFile(file, resourceType);
        } catch (IOException e) {
            throw new BaseException("Something went wrong. Try again", INTERNAL_ERROR);
        }
        return fileName;
    }

    private void checkSyllabusExists(GroupEntity group) {
        if (group.getSyllabus() != null) {
            throw new SyllabusAlreadyExistsException(group.getId());
        }
    }

    private void deleteFile(String filePath) {
        try {
            fileStorageService.deleteFile(filePath);
        } catch (IOException e) {
            throw new BaseException("Something went wrong. Try again", INTERNAL_ERROR);
        }
    }

    @Transactional
    public void deleteSchedule(Long id) {
        checkScheduleExistsById(id);

        scheduleRepository.deleteById(id);
    }

    @Transactional
    public Long createRecording(Long id, RecordingCreateRequest request) {
        checkGroupExists(id);

        String filePath = saveFile(request.file(), RECORDING);

        RecordingEntity entity = groupMapper.toRecordingEntity(id, filePath, request);

        recordingRepository.save(entity);
        return entity.getId();
    }

    public PageResponse<GroupCardResponse> search(GroupFilter filter, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("teacher.rating"));
        var responses = groupRepository.findAll(GroupSpecificationUtils.getSpecification(filter), pageable)
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
    public Long create(Long userId, GroupCreateRequest request) {
        teacherService.checkTeacherExists(userId);

        var group = groupMapper.toEntity(userId, request);

        return groupRepository.save(group).getId();
    }
}
