package com.adil.bridgespero.domain.model.dto.request;

import com.adil.bridgespero.domain.model.enums.Language;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record GroupEditRequest(
        String name,
        Long categoryId,
        Language language,
        LocalDate startDate,
        Integer durationMonths,
        Integer maxStudents,
        Double price,
        String description,
        MultipartFile image
) {}
