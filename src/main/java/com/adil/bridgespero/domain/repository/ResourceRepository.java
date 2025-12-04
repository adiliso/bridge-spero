package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.ResourceEntity;
import com.adil.bridgespero.domain.model.enums.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    List<ResourceEntity> findAllByGroupIdAndType(Long id, ResourceType type);
}
