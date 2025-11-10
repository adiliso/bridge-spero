package com.adil.bridgespero.domain.specification;

import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeacherSpecification {

    public static Specification<TeacherDetailEntity> hasName(String name) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("user").get("name")),
                        "%" + name.toLowerCase() + "%"))
        );
    }
}
