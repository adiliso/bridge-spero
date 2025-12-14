package com.adil.bridgespero.util;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.filter.GroupFilter;
import com.adil.bridgespero.domain.model.dto.filter.MyGroupsFilter;
import com.adil.bridgespero.domain.model.dto.filter.TeacherFilter;
import com.adil.bridgespero.domain.model.dto.filter.UserFilter;
import com.adil.bridgespero.domain.specification.GroupSpecification;
import com.adil.bridgespero.domain.specification.TeacherSpecification;
import com.adil.bridgespero.domain.specification.UserSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationUtils {

    public static Specification<GroupEntity> getGroupSpecification(GroupFilter filter) {
        Specification<GroupEntity> spec = Specification.allOf(GroupSpecification.isActive());

        if (filter.groupName() != null && !filter.groupName().isEmpty()) {
            spec = spec.and(GroupSpecification.hasName(filter.groupName()));
        }

        if (filter.language() != null) {
            spec = spec.and(GroupSpecification.hasLanguage(filter.language()));
        }

        if (filter.categoryId() != null) {
            spec = spec.and(GroupSpecification.hasCategoryId(filter.categoryId()));
        }

        return spec;
    }

    public static Specification<TeacherDetailEntity> getTeacherSpecification(TeacherFilter filter) {
        Specification<TeacherDetailEntity> spec = Specification.allOf();
        if (filter.name() != null && !filter.name().isEmpty()) {
            spec = spec.and(TeacherSpecification.hasName(filter.name()));
        }
        return spec;
    }

    public static Specification<GroupEntity> getTeacherGroupsSpecification(Long teacherId, MyGroupsFilter filter) {
        Specification<GroupEntity> spec = getMyGroupsSpecification(filter);

        spec = spec.and(GroupSpecification.hasTeacherId(teacherId));

        return spec;

    }

    public static Specification<GroupEntity> getStudentGroupsSpecification(Long userId, MyGroupsFilter filter) {
        Specification<GroupEntity> spec = getMyGroupsSpecification(filter);

        spec = spec.and(GroupSpecification.hasUserId(userId));

        return spec;
    }

    private static Specification<GroupEntity> getMyGroupsSpecification(MyGroupsFilter filter) {
        Specification<GroupEntity> spec = Specification.allOf();

        if (filter.groupName() != null && !filter.groupName().isEmpty()) {
            spec = spec.and(GroupSpecification.hasName(filter.groupName()));
        }

        if (filter.status() != null) {
            spec = spec.and(GroupSpecification.hasStatus(filter.status()));
        }

        if (filter.language() != null) {
            spec = spec.and(GroupSpecification.hasLanguage(filter.language()));
        }

        if (filter.categoryId() != null) {
            spec = spec.and(GroupSpecification.hasCategoryId(filter.categoryId()));
        }

        return spec;
    }

    public static Specification<UserEntity> getUserSpecification(UserFilter filter) {
        Specification<UserEntity> spec = Specification.allOf();
        if (filter.role() != null) {
            spec = spec.and(UserSpecification.hasRole(filter.role()));
        }
        return spec;
    }
}
