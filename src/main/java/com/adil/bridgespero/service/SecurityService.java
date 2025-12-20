package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.JoinRequestRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityService {

    GroupRepository groupRepository;
    ResourceRepository resourceRepository;
    UserService userService;
    GroupService groupService;
    ResourceService resourceService;
    ScheduleRepository scheduleRepository;
    private final JoinRequestRepository joinRequestRepository;

    public boolean isTeacherOfGroup(Long groupId) {
        groupService.checkGroupExists(groupId);

        Long userId = getCurrentUserId();
        return groupRepository.existsByIdAndTeacher_Id(groupId, userId);
    }

    public boolean isStudentOfGroup(Long groupId) {
        groupService.checkGroupExists(groupId);

        return groupRepository.existsByIdAndUsers_Id(groupId, getCurrentUserId());
    }

    public boolean isTeacherOfResource(Long id) {
        resourceService.checkResourceExists(id);

        return resourceRepository.existsByIdAndGroup_Teacher_Id(id, getCurrentUserId());
    }

    public boolean isStudentOfResource(Long id) {
        resourceService.checkResourceExists(id);

        return resourceRepository.existsByIdAndGroup_Users_Id(id, getCurrentUserId());
    }

    public Long getCurrentUserId() {
        return userService.getCurrentUserId();
    }

    public boolean isCurrentUser(Long userId) {
        return getCurrentUserId().equals(userId);
    }

    public boolean isTeacherOfSchedule(Long id) {
        groupService.checkScheduleExistsById(id);
        return scheduleRepository.existsByIdAndGroup_Teacher_Id(id, getCurrentUserId());
    }

    public boolean isTeacherOfJoinRequest(Long requestId) {
        return joinRequestRepository.existsByIdAndGroup_Teacher_Id(requestId, getCurrentUserId());
    }
}
