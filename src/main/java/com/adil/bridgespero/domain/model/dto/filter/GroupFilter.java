package com.adil.bridgespero.domain.model.dto.filter;

import com.adil.bridgespero.domain.model.enums.Language;

public record GroupFilter(

        String groupName,
        Language language,
        Long categoryId
) {
}
