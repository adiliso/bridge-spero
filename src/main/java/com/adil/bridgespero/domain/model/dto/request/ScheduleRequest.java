package com.adil.bridgespero.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ScheduleRequest(

        @NotBlank(message = "dayOfWeek is required")
        @Pattern(
                regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$",
                message = "dayOfWeek must be a valid day (e.g. Monday)"
        )
        String dayOfWeek,

        @NotBlank(message = "startTime is required")
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
                message = "startTime must be in HH:mm format (00:00 to 23:59)"
        )
        String startTime,

        @NotBlank(message = "endTime is required")
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
                message = "endTime must be in HH:mm format (00:00 to 23:59)"
        )
        String endTime
) {
}

