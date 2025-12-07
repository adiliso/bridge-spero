package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.ResourceRepository;
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

    private Long getCurrentUserId() {
        return userService.getCurrentUserId();
    }

    public boolean isCurrentUser(Long userId) {
        return getCurrentUserId().equals(userId);
    }
}
