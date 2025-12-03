package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.repository.GroupRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityService {

    GroupRepository groupRepository;
    UserService userService;

    public boolean isTeacherOfGroup(Long groupId) {
        Long userId = userService.getCurrentUserId();
        return groupRepository.existsByIdAndTeacher_Id(groupId, userId);
    }

    public boolean isStudentOfGroup(Long groupId) {
        return groupRepository.existsByIdAndUsers_Id(groupId, userService.getCurrentUserId());
    }
}
