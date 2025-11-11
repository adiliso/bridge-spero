package com.adil.bridgespero.domain.model.dto.request;

import com.adil.bridgespero.domain.model.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GroupCreateRequest(

        @NotBlank(message = "Group name is missing")
        String name,

        @NotBlank(message = "Category id is missing")
        Long categoryId,

        @NotBlank(message = "Language is missing")
        Language language,

        @NotBlank(message = "Start date is missing")
        @Schema(pattern = "dd.MM.yyyy")
        String startDate,

        @Positive(message = "Duration must be positive")
        Integer durationInMonths,

        @Min(value = 1, message = "Minimum students must be at least 1")
        Integer minStudents,

        @Min(value = 1, message = "Maximum students must be at least 1")
        Integer maxStudents,

        @NotNull(message = "Price must be provided")
        @Positive(message = "Price must be positive")
        Double price
) {
}