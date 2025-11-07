package com.adil.bridgespero.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ResourceType {

    SYLLABUS("/syllabuses", 10,
            List.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".jpg", ".jpeg", ".png"));

    private final String folder;
    private final long maxSizeInMb;
    private final List<String> allowedTypes;
}
