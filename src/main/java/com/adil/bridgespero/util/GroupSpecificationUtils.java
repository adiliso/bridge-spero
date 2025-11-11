package com.adil.bridgespero.util;

import com.adil.bridgespero.domain.entity.GroupEntity;
import com.adil.bridgespero.domain.model.dto.GroupFilter;
import com.adil.bridgespero.domain.specification.GroupSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupSpecificationUtils {

    public static Specification<GroupEntity> getSpecification(GroupFilter filter) {
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
}
