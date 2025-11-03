package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherDetailEntity, Long> {
}
