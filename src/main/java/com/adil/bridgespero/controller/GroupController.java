package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.service.GroupService;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {

    GroupService groupService;

    @GetMapping("/top-rated")
    public ResponseEntity<PageResponse<GroupCardResponse>> getTopRated(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize
    ) {
        return ResponseEntity.ok(groupService.getTopRated(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailsResponse> getDetails(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getDetailsById(id));
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<ScheduleResponse>> getSchedule(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getScheduleById(id));
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<String> getSyllabus(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getSyllabus(id));
    }

    @GetMapping("/{id}/recordings")
    public ResponseEntity<List<RecordingResponse>> getRecordings(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getRecordings(id));
    }
}
