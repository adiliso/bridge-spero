package com.adil.bridgespero.domain.model.dto.filter;

import com.adil.bridgespero.domain.model.enums.GroupStatus;
import com.adil.bridgespero.domain.model.enums.Language;

public record MyGroupsFilter(

        String groupName,
        GroupStatus status,
        Language language,
        Long categoryId
) {
}
