package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.request.AdminCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.CategoryCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.AdminResponse;
import com.adil.bridgespero.service.AdminService;
import com.adil.bridgespero.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    AdminService adminService;
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid AdminCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(request));
    }

    @GetMapping("/all")
    public List<AdminResponse> getAll() {
        return adminService.getAll();
    }

    @PostMapping("/category")
    public ResponseEntity<Long> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }
}
