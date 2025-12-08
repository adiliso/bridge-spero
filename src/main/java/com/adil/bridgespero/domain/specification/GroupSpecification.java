package com.adil.bridgespero.domain.specification;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.Language;
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

    public static Specification<GroupEntity> hasLanguage(Language language) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("language"), language)));
    }

    public static Specification<GroupEntity> hasCategoryId(Long categoryId) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("category").get("id"), categoryId)));
    }

    public static Specification<GroupEntity> hasStatus(GroupStatus status) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status)));
    }

    public static Specification<GroupEntity> hasTeacherId(Long teacherId) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("teacher").get("id"), teacherId)));
    }

    public static Specification<GroupEntity> hasUserId(Long userId) {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("users").get("id"), userId)));
    }
}
