package com.adil.bridgespero.util;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.entity.TeacherDetailEntity;
import com.adil.bridgespero.domain.model.dto.GroupFilter;
import com.adil.bridgespero.domain.model.dto.TeacherFilter;
import com.adil.bridgespero.domain.specification.GroupSpecification;
import com.adil.bridgespero.domain.specification.TeacherSpecification;
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

        if (filter.language() != null && !filter.language().isEmpty()) {
            spec = spec.and(GroupSpecification.hasLanguage(filter.language()));
        }

        if (filter.category() != null && !filter.category().isEmpty()) {
            spec = spec.and(GroupSpecification.hasCategory(filter.category()));
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
}
