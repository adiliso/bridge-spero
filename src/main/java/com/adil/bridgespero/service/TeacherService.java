package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.entity.TeacherRatingEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.TeacherDto;
import com.adil.bridgespero.domain.model.dto.TeacherFilter;
import com.adil.bridgespero.domain.model.dto.request.TeacherRateRequest;
import com.adil.bridgespero.domain.model.dto.request.TeacherUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherProfileResponse;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.domain.repository.GroupRepository;
import com.adil.bridgespero.domain.repository.ScheduleRepository;
import com.adil.bridgespero.domain.repository.TeacherRatingRepository;
import com.adil.bridgespero.domain.repository.TeacherRepository;
import com.adil.bridgespero.domain.repository.UserRepository;
import com.adil.bridgespero.exception.TeacherNotFoundException;
import com.adil.bridgespero.mapper.GroupMapper;
import com.adil.bridgespero.mapper.ScheduleMapper;
import com.adil.bridgespero.mapper.TeacherMapper;
import com.adil.bridgespero.util.SpecificationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class TeacherService {

    TeacherRepository teacherRepository;
    GroupRepository groupRepository;
    ScheduleRepository scheduleRepository;
    TeacherMapper teacherMapper;
    GroupMapper groupMapper;
    ScheduleMapper scheduleMapper;
    FileStorageService fileStorageService;
    private final UserService userService;
    private final TeacherRatingRepository teacherRatingRepository;
    private final UserRepository userRepository;

    public PageResponse<TeacherCardResponse> getTopRated(final int pageNumber, final int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("rating").descending());

        var responses = teacherRepository.findAll(pageable)
                .map(teacherMapper::toCardResponse);
        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages());
    }

    @PreAuthorize("hasRole('TEACHER')")
    public TeacherDashboardResponse getDashboard(Long id) {
        return teacherMapper.toDashboardResponse(
                getById(id), scheduleRepository.findAllByDayOfWeekAndGroup_Teacher_IdAndGroup_Status(DayOfWeek.from(LocalDate.now()), id, GroupStatus.ACTIVE)
                        .stream()
                        .map(scheduleMapper::toScheduleTeacherEventResponse)
                        .toList());
    }

    public List<GroupCardResponse> getGroups(Long id, MyGroupsFilter filter) {
        checkTeacherExists(id);
        return groupRepository.findAll(SpecificationUtils.getTeacherGroupsSpecification(id, filter))
                .stream()
                .map(groupMapper::toCardResponse)
                .toList();
    }

    @PreAuthorize("hasRole('TEACHER')")
    public ScheduleWeekResponse getSchedule(Long id) {
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        return scheduleMapper.toScheduleWeekResponse(
                startOfWeek,
                endOfWeek,
                scheduleRepository.findAllByGroup_Teacher_IdAndGroup_Status(
                        id,
                        GroupStatus.ACTIVE
                ));
    }

    public PageResponse<TeacherCardResponse> search(TeacherFilter filter, int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("rating").descending());

        var responses = teacherRepository.findAll(SpecificationUtils.getTeacherSpecification(filter), pageable)
                .map(teacherMapper::toCardResponse);

        return new PageResponse<>(
                responses.getContent(),
                pageNumber,
                pageSize,
                responses.getTotalElements(),
                responses.getTotalPages()
        );
    }

    public void checkTeacherExists(Long userId) {
        if (!teacherRepository.existsById(userId)) {
            throw new TeacherNotFoundException(userId);
        }
    }

    @Transactional
    public void save(TeacherDto teacherDto, MultipartFile demoVideo) {
        String demoVideoUrl = null;
        if (demoVideo != null && !demoVideo.isEmpty()) {
            demoVideoUrl = fileStorageService.saveFile(demoVideo, ResourceType.DEMO_VIDEO);
        }
        teacherDto.setDemoVideoUrl(demoVideoUrl);

        var entity = teacherMapper.toEntity(teacherDto);
        teacherRepository.save(entity);
    }

    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public String updateDemoVideo(MultipartFile demoVideo, Long userId) {
        var entity = getById(userId);

        String oldDemoVideoUrl = entity.getDemoVideoUrl();

        String newDemoVideUrl = fileStorageService.saveFile(demoVideo, ResourceType.DEMO_VIDEO);
        entity.setDemoVideoUrl(newDemoVideUrl);
        teacherRepository.save(entity);

        System.err.println(oldDemoVideoUrl);
        if (oldDemoVideoUrl != null && !oldDemoVideoUrl.isEmpty()) {
            System.err.println(oldDemoVideoUrl);
            fileStorageService.deleteFile(oldDemoVideoUrl);
        }
        return newDemoVideUrl;
    }

    private TeacherDetailEntity getById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFoundException(id));
    }

    public TeacherProfileResponse getProfile(Long id) {
        var teacher = getById(id);
        return teacherMapper.toProfileResponse(teacher);
    }

    public List<GroupCardResponse> getProfileGroups(Long id) {
        checkTeacherExists(id);
        MyGroupsFilter filter = new MyGroupsFilter(null, GroupStatus.ACTIVE, null, null);
        return getGroups(id, filter);
    }

    @Transactional
    @PreAuthorize("!@securityService.isCurrentUser(#teacherId)")
    public void rate(Long userId, Long teacherId, TeacherRateRequest request) {
        UserEntity user = userService.findById(userId);
        TeacherDetailEntity teacher = getById(teacherId);

        TeacherRatingEntity rating = teacherRatingRepository
                .findByTeacherIdAndUserId(teacherId, userId)
                .orElse(new TeacherRatingEntity());

        rating.setTeacher(teacher);
        rating.setUser(user);
        rating.setRating(request.rating());

        teacherRatingRepository.save(rating);

        teacher.setRating(getTeacherDisplayedRating(teacherId));
        teacherRepository.save(teacher);
    }

    public double getTeacherDisplayedRating(Long teacherId) {
        double globalAvg = 4.5;
        int weight = 5;

        Double realAvg = teacherRatingRepository.getTeacherAverageRating(teacherId);
        Long count = teacherRatingRepository.countByTeacher_Id(teacherId);

        double newAvg = (globalAvg * weight + realAvg * count) / (weight + count);
        return Math.round(newAvg * 100.0) / 100.0;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#teacherId)")
    public void update(Long teacherId, TeacherUpdateRequest request) {
        checkTeacherExists(teacherId);

        TeacherDetailEntity teacher = getById(teacherId);
        UserEntity user = teacher.getUser();

        if (!user.getEmail().equals(request.getEmail())) {
            userService.checkEmailAlreadyExists(request.getEmail());
        }

        teacherMapper.update(user, teacher, request);

        userRepository.save(user);
        teacherRepository.save(teacher);
    }
}
