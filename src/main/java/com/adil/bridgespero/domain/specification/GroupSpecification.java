package com.adil.bridgespero.domain.specification;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupSpecification {

    public static Specification<GroupEntity> isActive() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), GroupStatus.ACTIVE));
    }

    public static Specification<GroupEntity> hasName(String name) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%")));
    }

    public static Specification<GroupEntity> hasLanguage(String language) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("language")),
                        language.toLowerCase())));
    }

    public static Specification<GroupEntity> hasCategory(String category) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")),
                        category.toLowerCase())));
    }
}
