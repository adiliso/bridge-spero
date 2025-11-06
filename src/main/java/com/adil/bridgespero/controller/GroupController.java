package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.RecordingResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.service.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static com.adil.bridgespero.domain.model.constant.PageConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;

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
        return ResponseEntity.ok(groupService.getScheduleByGroupId(id));
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request
    ) {
        return ResponseEntity.status(CREATED).body(groupService.createSchedule(id, request));
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request
    ){
        groupService.updateSchedule(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<String> getSyllabus(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getSyllabus(id));
    }

    @PostMapping(value = "{id}/syllabus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createSyllabus(
            @PathVariable Long id,
            SyllabusCreateRequest request
    ){
        return ResponseEntity.status(CREATED).body(groupService.createSyllabus(id, request));
    }

    @GetMapping("/{id}/recordings")
    public ResponseEntity<List<RecordingResponse>> getRecordings(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getRecordings(id));
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<List<ResourceResponse>> getResources(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getResources(id));
    }
}
