package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.RecordingRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.exception.GroupNotFoundException;
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
    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;

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

    public GroupEntity getById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    public List<ScheduleResponse> getScheduleById(Long groupId) {
        return scheduleRepository.findAllByGroupId(groupId)
                .stream()
                .map(scheduleMapper::toScheduleResponse)
                .toList();
    }

    public String getSyllabus(Long id) {
        return getById(id).getSyllabus().toString();
    }

    public List<RecordingResponse> getRecordings(Long id) {
        return recordingRepository.findAllByGroupId(id)
                .stream()
                .map(groupMapper::toRecordingResponse)
                .toList();
    }
}
