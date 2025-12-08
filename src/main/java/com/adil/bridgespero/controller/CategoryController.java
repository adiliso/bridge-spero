package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.request.CategoryCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.CategoryUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.CategoryResponse;
import com.adil.bridgespero.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@RequestParam(required = false) Long parentId) {
        return ResponseEntity.ok(categoryService.getAllByParentId(parentId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
