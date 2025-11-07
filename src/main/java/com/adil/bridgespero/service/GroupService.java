package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.RecordingRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.exception.GroupNotFoundException;
import com.adil.bridgespero.exception.ScheduleNotFoundException;
import com.adil.bridgespero.exception.SyllabusAlreadyExistsException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE;

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
        return getById(id).getSyllabus().toString();
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

        String fileName = fileStorageService.savePublicFile(request.file(), "/syllabuses");

        group.setSyllabus(fileName);

        return fileName;
    }

    private void checkSyllabusExists(GroupEntity group) {
        if (group.getSyllabus() != null) {
            throw new SyllabusAlreadyExistsException(group.getId());
        }
    }
}
