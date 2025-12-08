package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.ScheduleEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByGroupId(Long groupId);

    List<ScheduleEntity> findAllByDayOfWeekAndGroup_Teacher_IdAndGroup_Status(DayOfWeek dayOfWeek, Long teacherId, GroupStatus status);

    List<ScheduleEntity> findAllByDayOfWeekAndGroup_Users_IdAndGroup_Status(DayOfWeek dayOfWeek, Long userid, GroupStatus status);

    List<ScheduleEntity> findAllByGroup_Teacher_IdAndGroup_Status(Long teacherId, GroupStatus status);

    List<ScheduleEntity> findAllByGroup_Users_IdAndGroup_Status(Long userId, GroupStatus status);

    boolean existsByIdAndGroup_Teacher_Id(Long id, Long currentUserId);
}
