package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.GroupTeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherCardResponse;
import com.adil.bridgespero.domain.model.dto.response.TeacherDashboardResponse;
import com.adil.bridgespero.service.TeacherService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<PageResponse<TeacherCardResponse>> getTopRated(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize
    ) {
        return ResponseEntity.ok(teacherService.getTopRated(pageNumber, pageSize));
    }

    @GetMapping
    public ResponseEntity<TeacherDashboardResponse> getDashboard(@RequestHeader("User-Id") Long id) {
        return ResponseEntity.ok(teacherService.getDashboard(id));
    }

    @GetMapping
    public ResponseEntity<List<GroupTeacherCardResponse>> getGroups(
            @RequestHeader("User-Id") Long id,
            @RequestParam int parameter) {
        return ResponseEntity.ok(teacherService.getGroups(id, parameter));
    }
}
