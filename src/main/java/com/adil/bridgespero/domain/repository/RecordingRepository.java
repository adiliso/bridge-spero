package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.RecordingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository extends JpaRepository<RecordingEntity, Long> {

    List<RecordingEntity> findAllByGroupId(Long id);
}
