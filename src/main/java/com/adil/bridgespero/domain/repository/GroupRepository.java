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

    @Modifying
    @Query(value = """
            DELETE FROM group_user gu where gu.group_id = :groupId AND gu.user_id = :studentId
            """, nativeQuery = true)
    void deleteMembership(@Param("groupId") Long id, @Param("studentId") Long studentId);
}
