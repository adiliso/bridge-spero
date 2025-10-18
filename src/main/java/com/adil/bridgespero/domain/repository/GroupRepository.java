package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    List<GroupEntity> findAllByTeacherIdAndStatus(Long teacherId, GroupStatus status);

    @Query("SELECT g FROM GroupEntity g " +
           "WHERE g.teacher.id = :teacherId " +
           "AND g.status IN (:statuses) " +
           "AND :date BETWEEN g.startDate AND g.endDate")
    List<GroupEntity> findAllByTeacherIdAndStatusInAndDateBetweenStartAndEnd(
            @Param("teacherId") Long teacherId,
            @Param("statuses") List<GroupStatus> statuses,
            @Param("date") LocalDate date
    );
}
