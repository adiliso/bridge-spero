package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.CategoryEntity;
import com.adil.bridgespero.domain.model.dto.request.CategoryCreateRequest;
import com.adil.bridgespero.domain.repository.CategoryRepository;
import com.adil.bridgespero.exception.CategoryNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Long create(CategoryCreateRequest request) {
        Long id = request.parentId();
        var entity = CategoryEntity.builder()
                .name(request.name())
                .parent(id == null ? null : getById(id))
                .build();
        return categoryRepository.save(entity).getId();
    }

    public CategoryEntity getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );
    }

    public void checkCategoryExists(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
    }
}
