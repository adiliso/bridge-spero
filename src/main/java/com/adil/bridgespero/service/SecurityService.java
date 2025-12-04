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

    public boolean isTeacherOfGroup(Long groupId) {
        Long userId = getCurrentUserId();
        return groupRepository.existsByIdAndTeacher_Id(groupId, userId);
    }

    public boolean isStudentOfGroup(Long groupId) {
        return groupRepository.existsByIdAndUsers_Id(groupId, getCurrentUserId());
    }

    public boolean isTeacherOfResource(Long id) {
        return resourceRepository.existsByIdAndGroup_Teacher_Id(id, getCurrentUserId());
    }

    public boolean isStudentOfResource(Long id) {
        return resourceRepository.existsByIdAndGroup_Users_Id(id, getCurrentUserId());
    }

    private Long getCurrentUserId() {
        return userService.getCurrentUserId();
    }
}
