package com.adil.bridgespero.domain.model.dto.response;

import org.springframework.core.io.Resource;

public record ResourceWithMeta(

        Resource resource,
        ResourceResponse meta
) {
}
