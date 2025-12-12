package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.TeacherRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRatingRepository extends JpaRepository<TeacherRatingEntity, Long> {

    Optional<TeacherRatingEntity> findByTeacherIdAndUserId(Long teacherId, Long userId);


    @Query("""
            SELECT AVG(r.rating) FROM TeacherRatingEntity r where r.teacher.id=:teacherId
            """)
    Double getTeacherAverageRating(Long teacherId);

    Long countByTeacher_Id(Long teacherId);
}
