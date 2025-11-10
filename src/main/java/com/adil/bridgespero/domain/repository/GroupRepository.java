package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {

    List<GroupEntity> findAllByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    Page<GroupEntity> findAllByStatus(GroupStatus status, Pageable pageable);

    @Query("""
                SELECT g
                FROM GroupEntity g
                JOIN g.lessonSchedules ls
                WHERE g.teacher.id = :teacherId
                AND ls.dayOfWeek = :dayOfWeek
                AND g.status = com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE
            """)
    List<GroupEntity> findAllByTeacherIdAndStatusAndDayOfWeek(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek);

    @Query("""
                SELECT g
                FROM GroupEntity g
                WHERE g.teacher.id = :teacherId
                  AND (
                      g.status = com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE
                      OR (
                          g.startDate <= :endOfWeek
                          AND g.endDate >= :startOfWeek
                      )
                  )
            """)
    List<GroupEntity> findAllByTeacherIdAndSchedule(
            @Param("teacherId") Long teacherId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );
}
