package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.JoinRequestEntity;
import com.adil.bridgespero.domain.model.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequestEntity, Long> {

    boolean existsByIdAndGroup_Teacher_Id(Long id, Long teacherId);

    List<JoinRequestEntity> getAllByGroup_Teacher_IdAndStatus(Long teacherId, JoinRequestStatus status);

    boolean existsByStudent_IdAndGroup_IdAndStatus(Long studentId, Long groupId, JoinRequestStatus status);

    void deleteByGroup_IdAndStudent_IdAndStatus(Long groupId, Long studentId, JoinRequestStatus status);
}
