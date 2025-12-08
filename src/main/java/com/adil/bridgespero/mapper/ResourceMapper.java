package com.adil.bridgespero.mapper;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.ResourceEntity;
import com.adil.bridgespero.domain.model.dto.request.ResourceCreateRequest;
import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

import static com.adil.bridgespero.constant.AppConstant.GROUP_DATE_FORMATTER;

@Component
@RequiredArgsConstructor
public class ResourceMapper {

    private final FileStorageService fileStorageService;

    public ResourceResponse toResponse(ResourceEntity entity) {
        return new ResourceResponse(
                entity.getId(),
                entity.getName(),
                entity.getPath(),
                entity.getContentType(),
                LocalDate.ofInstant(entity.getCreatedAt(), ZoneId.of("UTC+4")).format(GROUP_DATE_FORMATTER)
        );
    }

    public ResourceEntity toEntity(Long groupId, String path, ResourceType type, ResourceCreateRequest request) {
        return ResourceEntity.builder()
                .name(request.name())
                .path(path)
                .type(type)
                .contentType(fileStorageService.probeContentType(path))
                .group(GroupEntity.builder()
                        .id(groupId)
                        .build())
                .build();
    }
}
