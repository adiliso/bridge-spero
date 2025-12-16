package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.filter.GroupFilter;
import com.adil.bridgespero.domain.model.dto.request.GroupCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.GroupEditRequest;
import com.adil.bridgespero.domain.model.dto.request.ResourceCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.ScheduleRequest;
import com.adil.bridgespero.domain.model.dto.request.SyllabusCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupDetailsResponse;
import com.adil.bridgespero.domain.model.dto.response.GroupMembersResponse;
import com.adil.bridgespero.domain.model.dto.response.PageResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleResponse;
import com.adil.bridgespero.domain.model.dto.response.StudentIsMemberResponse;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.service.GroupService;
import com.adil.bridgespero.service.SecurityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.adil.bridgespero.constant.PageConstant.DEFAULT_PAGE_NUMBER;
import static com.adil.bridgespero.constant.PageConstant.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupController {

    GroupService groupService;
    SecurityService securityService;

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

    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> edit(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserPrincipal user,
            @Valid @ModelAttribute GroupEditRequest request
    ) {
        groupService.edit(id, user.getId(), request);
        return ResponseEntity.ok().build();
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
    ) {
        groupService.updateSchedule(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id
    ) {
        groupService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<String> getSyllabus(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getSyllabus(id));
    }

    @PostMapping(value = "/{id}/syllabus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createSyllabus(
            @PathVariable Long id,
            SyllabusCreateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(groupService.createSyllabus(id, request));
    }

    @DeleteMapping("/{id}/syllabus")
    public ResponseEntity<Void> deleteSyllabus(@PathVariable Long id) {
        groupService.deleteSyllabus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/recordings")
    public ResponseEntity<List<ResourceResponse>> getRecordings(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getResources(id, ResourceType.RECORDING));
    }

    @PostMapping(value = "/{id}/recordings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createRecording(
            @PathVariable Long id,
            @Valid @ModelAttribute ResourceCreateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(groupService.createRecording(id, request));
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<List<ResourceResponse>> getResources(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(groupService.getResources(id, ResourceType.RESOURCES));
    }

    @PostMapping(value = "/{id}/resources", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createResource(
            @PathVariable Long id,
            @Valid ResourceCreateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(groupService.createResource(id, request));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<GroupCardResponse>> search(
            @ParameterObject GroupFilter filter,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize
    ) {
        return ResponseEntity.ok(groupService.search(filter, pageNumber, pageSize));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> create(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @Valid @ModelAttribute GroupCreateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(groupService.create(user.getId(), request));
    }

    @GetMapping("{id}/start-lesson")
    public ResponseEntity<String> startLesson(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(groupService.startLesson(id, user.getUsername()));
    }

    @GetMapping("{id}/join-lesson")
    public ResponseEntity<String> joinLesson(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.joinLesson(id));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal user) {
        groupService.addMember(id, user.getId());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<GroupMembersResponse> getAllMembers(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getAllMembers(id));
    }

    @DeleteMapping("/{id}/members/{studentId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id, @PathVariable Long studentId) {
        groupService.deleteMembership(id, studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/is-member")
    public ResponseEntity<StudentIsMemberResponse> isMember(@PathVariable Long id) {
        boolean isMember = securityService.isStudentOfGroup(id);
        return ResponseEntity.ok(new StudentIsMemberResponse(isMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
