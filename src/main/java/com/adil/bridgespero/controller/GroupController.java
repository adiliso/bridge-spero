package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.service.GroupService;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping()
    public ResponseEntity<GroupDetailsResponse> getDetails(
            @RequestParam Long groupId
    ) {
        return ResponseEntity.ok(groupService.getDetailsById(groupId));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedule(
            @RequestParam Long groupId
    ) {
        return ResponseEntity.ok(groupService.getScheduleById(groupId));
    }
}
