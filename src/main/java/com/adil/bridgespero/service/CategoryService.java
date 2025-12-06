package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.CategoryEntity;
import com.adil.bridgespero.domain.model.dto.request.CategoryCreateRequest;
import com.adil.bridgespero.domain.model.dto.request.CategoryUpdateRequest;
import com.adil.bridgespero.domain.model.dto.response.CategoryResponse;
import com.adil.bridgespero.domain.repository.CategoryRepository;
import com.adil.bridgespero.exception.CategoryNameAlreadyExistsException;
import com.adil.bridgespero.exception.CategoryNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Long create(CategoryCreateRequest request) {
        checkCategoryNameExists(request.name());
        Long id = request.parentId();
        var entity = CategoryEntity.builder()
                .name(request.name())
                .parent(id == null ? null : getById(id))
                .build();
        return categoryRepository.save(entity).getId();
    }

    private void checkCategoryNameExists(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new CategoryNameAlreadyExistsException(name);
        }
    }

    public CategoryEntity getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id)
        );
    }

    public void checkCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
    }

    public List<CategoryResponse> getAllByParentId(Long parentId) {
        var categories = parentId == null ? categoryRepository.findAllByParentIdIsNull() :
                categoryRepository.findAllByParentId(parentId);
        return categories
                .stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateById(Long id, CategoryUpdateRequest request) {
        var category = getById(id);
        checkCategoryNameExists(request.name());

        category.setName(request.name());
        return new CategoryResponse(category.getId(), category.getName());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        checkCategoryExists(id);

        categoryRepository.deleteById(id);
    }
}
