package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.LessonScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<LessonScheduleEntity, Long> {

    List<LessonScheduleEntity> findAllByGroupId(Long groupId);
}
