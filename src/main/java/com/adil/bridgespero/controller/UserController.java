package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.request.UserUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.domain.model.dto.response.UserProfileResponse;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(userService.getById(user.getId()));
    }

    @GetMapping("/schedule")
    public ResponseEntity<ScheduleWeekResponse> getSchedule(@AuthenticationPrincipal CustomUserPrincipal user) {
        ScheduleWeekResponse schedule = userService.getSchedule(user.getId());
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UserDashboardResponse> getDashboard(@AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(userService.getDashboard(user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupCardResponse>> getGroups(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @ParameterObject MyGroupsFilter filter) {
        return ResponseEntity.ok(userService.getGroups(user.getId(), filter));
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createImage(@AuthenticationPrincipal CustomUserPrincipal user,
                                              @RequestPart("file") @NotNull MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createProfilePicture(user.getId(), file));
    }

    @DeleteMapping(value = "/profile-picture")
    public ResponseEntity<Void> deleteImage(@AuthenticationPrincipal CustomUserPrincipal user) {
        userService.deleteProfilePicture(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateImage(@AuthenticationPrincipal CustomUserPrincipal user,
                                              @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePicture(user.getId(), file));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GroupCardResponse>> getProfileGroups(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfileGroups(id));
    }

    @PostMapping(value = "/background-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBackgroundImage(@AuthenticationPrincipal CustomUserPrincipal user,
                                                        @RequestPart("file") @NotNull MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createBackgroundImage(user.getId(), file));
    }

    @DeleteMapping(value = "/background-image")
    public ResponseEntity<Void> deleteBackgroundImage(@AuthenticationPrincipal CustomUserPrincipal user) {
        userService.deleteBackgroundImage(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/background-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBackgroundImage(@AuthenticationPrincipal CustomUserPrincipal user,
                                                        @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateBackgroundImage(user.getId(), file));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getAllActive(){
        return ResponseEntity.ok(userService.getAllActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        userService.update(id, request);
        return ResponseEntity.noContent().build();
    }

}
