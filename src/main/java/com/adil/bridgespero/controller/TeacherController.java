package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.TeacherFilter;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.service.TeacherService;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.adil.bridgespero.constant.PageConstant.DEFAULT_PAGE_NUMBER;
import static com.adil.bridgespero.constant.PageConstant.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/teachers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherController {

    TeacherService teacherService;

    @GetMapping("/top-rated")
    public ResponseEntity<PageResponse<TeacherCardResponse>> getTopRated(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize
    ) {
        return ResponseEntity.ok(teacherService.getTopRated(pageNumber, pageSize));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<TeacherDashboardResponse> getDashboard(@AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(teacherService.getDashboard(user.getId()));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupCardResponse>> getGroups(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject MyGroupsFilter filter) {
        return ResponseEntity.ok(teacherService.getGroups(user.getId(), filter));
    }

    @GetMapping("/schedule")
    public ResponseEntity<ScheduleWeekResponse> getSchedule(
            @AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(teacherService.getSchedule(user.getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<TeacherCardResponse>> search(
            @ParameterObject TeacherFilter filter,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize) {
        return ResponseEntity.ok(teacherService.search(filter, pageNumber, pageSize));
    }

    @PutMapping(value = "/demo-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateDemoVideo(
            @ModelAttribute MultipartFile demoVideo,
            @AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(teacherService.updateDemoVideo(demoVideo, user.getId()));
    }
}
