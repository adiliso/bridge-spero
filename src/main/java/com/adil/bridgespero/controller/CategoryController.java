package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.CategoryResponse;
import com.adil.bridgespero.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<List<CategoryResponse>> getAll(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getAllByParentId(id));
    }
}
