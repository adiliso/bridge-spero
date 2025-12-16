package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.UserInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {

    Optional<UserInterestEntity> findByUser_IdAndCategory_Id(Long userId, Long categoryId);

    void deleteAllByUserId(Long userId);
}
