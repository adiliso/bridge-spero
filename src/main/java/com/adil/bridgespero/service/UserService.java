package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.EmailAlreadyExistsException;
import com.adil.bridgespero.exception.UserNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.UserMapper;
import com.adil.bridgespero.security.mapper.UserMapper2;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    GroupRepository groupRepository;
    UserMapper userMapper;
    UserMapper2 userMapper2;
    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;

    public ScheduleWeekResponse getSchedule(Long id) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        var weekGroups = groupRepository.findAllByUserIdAndSchedule(id, startOfWeek, endOfWeek);
        return scheduleMapper.toScheduleWeekResponse(startOfWeek, endOfWeek, weekGroups);
    }

    public UserDashboardResponse getDashboard(Long id) {
        checkUserExists(id);

        var groups = groupRepository.findAllByUserIdAndStatusAndDayOfWeek(id, DayOfWeek.from(LocalDate.now()));

        return userMapper.toDashboardResponse(findById(id), groups
                .stream()
                .map(groupMapper::toGroupUserDashboardResponse)
                .toList());
    }

    public boolean isEmailExist(final String email) {
        return userRepository.existsByEmail(email);
    }

    private UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void checkUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper2.toEntity(userDto);
        return userMapper2.toDto(userRepository.save(userEntity));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        checkUserExists(id);
        userRepository.deleteById(id);
    }

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserPrincipal) {
            return ((CustomUserPrincipal) principal).getId();
        }

        throw new AccessDeniedException("Authenticated principal is not of the expected CustomUserDetails type.");
    }

    public void checkEmailAlreadyExists(String email) {
        if (isEmailExist(email)) throw new EmailAlreadyExistsException();
    }
}
