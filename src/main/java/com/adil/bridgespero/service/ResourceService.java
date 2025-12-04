package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.entity.ResourceEntity;
import com.adil.bridgespero.domain.model.dto.response.ResourceWithMeta;
import com.adil.bridgespero.domain.repository.ResourceRepository;
import com.adil.bridgespero.exception.ResourceNotFoundException;
import com.adil.bridgespero.mapper.ResourceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceService {

    ResourceRepository resourceRepository;
    FileStorageService fileStorageService;
    ResourceMapper resourceMapper;

    @PreAuthorize("hasRole('ADMIN') or securityService.isTeacherOfResource(#id)" +
                  " or securityService.isStudentOfResource(#id)")
    public ResourceWithMeta load(Long id) {
        var resourceEntity = getById(id);

        Resource file = fileStorageService.load(resourceEntity.getPath());

        return new ResourceWithMeta(
                file,
                resourceMapper.toResponse(resourceEntity)
        );
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or securityService.isTeacherOfResource(#id)")
    public void deleteById(Long id) {
        var resourceEntity = getById(id);

        fileStorageService.deleteFile(resourceEntity.getPath());
        resourceRepository.deleteById(id);
    }

    public ResourceEntity getById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
