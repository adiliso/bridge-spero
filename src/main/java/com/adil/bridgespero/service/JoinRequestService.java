package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.JoinRequestEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.response.JoinNotificationResponse;
import com.adil.bridgespero.domain.model.enums.JoinRequestStatus;
import com.adil.bridgespero.domain.repository.JoinRequestRepository;
import com.adil.bridgespero.exception.RequestDuplicationException;
import com.adil.bridgespero.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinRequestService {

    private final GroupService groupService;
    private final UserService userService;
    private final TeacherService teacherService;
    private final JoinRequestRepository joinRequestRepository;

    @Transactional
    public Long create(Long groupId, Long studentId) {
        GroupEntity group = groupService.getById(groupId);
        UserEntity user = userService.findById(studentId);
        groupService.checkUserGroupAlreadyExists(studentId, groupId);
        groupService.checkGroupIsFull(groupId, group.getMaxStudents());
        checkNoPendingRequest(groupId, studentId);

        JoinRequestEntity joinRequest = JoinRequestEntity.builder()
                .group(group)
                .student(user)
                .status(JoinRequestStatus.PENDING)
                .build();
        var saved = joinRequestRepository.save(joinRequest);
        return saved.getId();
    }

    private void checkNoPendingRequest(Long groupId, Long studentId) {
        if (joinRequestRepository.existsByStudent_IdAndGroup_IdAndStatus(studentId,
                groupId,
                JoinRequestStatus.PENDING)) {
            throw new RequestDuplicationException(studentId, groupId);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfJoinRequest(#requestId)")
    public void accept(Long requestId) {
        JoinRequestEntity entity = findById(requestId);

        groupService.addMember(entity.getGroup().getId(), entity.getStudent().getId());

        entity.setStatus(JoinRequestStatus.ACCEPTED);
        joinRequestRepository.save(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTeacherOfJoinRequest(#requestId)")
    public void decline(Long requestId) {
        JoinRequestEntity entity = findById(requestId);

        entity.setStatus(JoinRequestStatus.DECLINED);
        joinRequestRepository.save(entity);
    }

    public JoinRequestEntity findById(Long requestId) {
        return joinRequestRepository.findById(requestId).orElseThrow(
                () -> new RequestNotFoundException(requestId));
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public List<JoinNotificationResponse> getAllByTeacherId(Long id) {
        teacherService.checkTeacherExists(id);

        var entities = joinRequestRepository.getAllByGroup_Teacher_IdAndStatus(id, JoinRequestStatus.PENDING);

        return entities.stream()
                .map(e -> new JoinNotificationResponse(
                        e.getId(),
                        e.getStudent().getName(),
                        e.getStudent().getSurname(),
                        e.getGroup().getName()
                ))
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#studentId)")
    public void deleteRequest(Long groupId, Long studentId) {
        joinRequestRepository.deleteByGroup_IdAndStudent_IdAndStatus(groupId, studentId, JoinRequestStatus.PENDING);
    }
}
