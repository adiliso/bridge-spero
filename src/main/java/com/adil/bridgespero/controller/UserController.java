package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.UserDto;
import com.adil.bridgespero.domain.model.dto.response.GroupCardResponse;
import com.adil.bridgespero.domain.model.dto.response.ScheduleWeekResponse;
import com.adil.bridgespero.domain.model.dto.response.UserDashboardResponse;
import com.adil.bridgespero.security.model.CustomUserPrincipal;
import com.adil.bridgespero.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
