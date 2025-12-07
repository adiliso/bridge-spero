package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {

    List<GroupEntity> findAllByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    Page<GroupEntity> findAllByStatus(GroupStatus status, Pageable pageable);

    Optional<GroupEntity> findByMeetingId(Long currentMeetingId);

    boolean existsByIdAndTeacher_Id(Long groupId, Long teacherId);

    boolean existsByIdAndUsers_Id(Long groupId, Long userId);

    @Query("SELECT COUNT(u) FROM GroupEntity g JOIN g.users u WHERE g.id = :groupId")
    int countUsersInGroup(Long groupId);

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
                JOIN g.users u
                JOIN g.lessonSchedules ls
                WHERE u.id = :userId
                AND ls.dayOfWeek = :dayOfWeek
                AND g.status = com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE
            """)
    List<GroupEntity> findAllByUserIdAndStatusAndDayOfWeek(
            @Param("userId") Long userId,
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

    @Query("""
                SELECT g
                FROM GroupEntity g
                JOIN g.users u
                WHERE u.id = :userId
                  AND (
                      g.status = com.adil.bridgespero.domain.model.enums.GroupStatus.ACTIVE
                      OR (
                          g.startDate <= :endOfWeek
                          AND g.endDate >= :startOfWeek
                      )
                  )
            """)
    List<GroupEntity> findAllByUserIdAndSchedule(
            @Param("userId") Long userId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );

    @Modifying
    @Query(value = """
            DELETE FROM group_user gu where gu.group_id = :groupId AND gu.user_id = :studentId
            """, nativeQuery = true)
    void deleteMembership(@Param("groupId") Long id, @Param("studentId") Long studentId);
}
