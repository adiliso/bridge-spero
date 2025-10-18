package com.adil.bridgespero.domain.model.dto.response;

import java.util.List;

public record PageResponse<T>(

        List<T> data,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPagesCount
) {
}
