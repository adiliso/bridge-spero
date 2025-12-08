package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByRole(Role role);

    List<UserEntity> findAllByGroups_Id(Long groupId);

    boolean existsByEmail(String email);
}
