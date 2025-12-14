package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.request.UserUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.UserProfileResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.EmailAlreadyExistsException;
import com.adil.bridgespero.exception.UserNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.UserMapper;
import com.adil.bridgespero.security.mapper.UserMapper2;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.util.SpecificationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    GroupRepository groupRepository;
    ScheduleRepository scheduleRepository;
    UserMapper userMapper;
    UserMapper2 userMapper2;
    ScheduleMapper scheduleMapper;
    GroupMapper groupMapper;
    FileStorageService fileStorageService;

    public ScheduleWeekResponse getSchedule(Long id) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        var schedules = scheduleRepository.findAllByGroup_Users_IdAndGroup_Status(id, GroupStatus.ACTIVE);
        return scheduleMapper.toScheduleWeekResponse(startOfWeek, endOfWeek, schedules);
    }

    public UserDashboardResponse getDashboard(Long id) {
        checkUserExists(id);

        var schedules = scheduleRepository.findAllByDayOfWeekAndGroup_Users_IdAndGroup_Status(
                DayOfWeek.from(LocalDate.now()),
                id, GroupStatus.ACTIVE);

        return userMapper.toDashboardResponse(
                findById(id),
                schedules.stream()
                        .map(scheduleMapper::toScheduleUserEventResponse)
                        .toList()
        );
    }

    public boolean isEmailExist(final String email) {
        return userRepository.existsByEmail(email);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void checkUserExists(Long id) {
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

    public UserDto getById(Long id) {
        var entity = findById(id);
        return userMapper2.toDto(entity);
    }

    public List<GroupCardResponse> getGroups(Long userId, MyGroupsFilter filter) {
        return groupRepository.findAll(SpecificationUtils.getStudentGroupsSpecification(userId, filter))
                .stream()
                .map(groupMapper::toCardResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Transactional
    public String createProfilePicture(Long id, MultipartFile file) {
        UserEntity user = findById(id);
        String path = fileStorageService.saveFile(file, ResourceType.IMAGE);

        user.setProfilePictureUrl(path);
        return path;
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Transactional
    public void deleteProfilePicture(Long id) {
        UserEntity user = findById(id);

        fileStorageService.deleteFile(user.getProfilePictureUrl());

        user.setProfilePictureUrl(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public String updateProfilePicture(Long id, MultipartFile file) {
        UserEntity user = findById(id);

        String oldImage = user.getProfilePictureUrl();
        String newImage = fileStorageService.saveFile(file, ResourceType.IMAGE);
        fileStorageService.deleteFile(oldImage);

        user.setProfilePictureUrl(newImage);
        return newImage;
    }

    public UserProfileResponse getProfile(Long id) {
        UserEntity user = findById(id);

        return userMapper.toProfileResponse(user);
    }

    public List<GroupCardResponse> getProfileGroups(Long id) {
        checkUserExists(id);
        MyGroupsFilter filter = new MyGroupsFilter(null, null, null, null);
        return getGroups(id, filter);
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Transactional
    public String createBackgroundImage(Long id, MultipartFile file) {
        UserEntity user = findById(id);
        String path = fileStorageService.saveFile(file, ResourceType.IMAGE);

        user.setBackgroundImageUrl(path);
        return path;
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Transactional
    public void deleteBackgroundImage(Long id) {
        UserEntity user = findById(id);

        fileStorageService.deleteFile(user.getBackgroundImageUrl());

        user.setBackgroundImageUrl(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public String updateBackgroundImage(Long id, MultipartFile file) {
        UserEntity user = findById(id);

        String oldImage = user.getBackgroundImageUrl();
        String newImage = fileStorageService.saveFile(file, ResourceType.IMAGE);
        fileStorageService.deleteFile(oldImage);

        user.setBackgroundImageUrl(newImage);
        return newImage;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAll() {
        var userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(userMapper2::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public void update(Long id, UserUpdateRequest request) {
        checkUserExists(id);

        UserEntity user = findById(id);

        userMapper.update(user, request);
        userRepository.save(user);
    }
}
