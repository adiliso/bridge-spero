package com.adil.bridgespero.domain.specification;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserEntity> hasRole(Role role) {
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }
}
