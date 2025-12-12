package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TeacherRateRequest(

        @Min(1) @Max(5) int rating
) {
}
