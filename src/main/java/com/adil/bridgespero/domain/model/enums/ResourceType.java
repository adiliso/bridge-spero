package com.adil.bridgespero.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ResourceType {

    IMAGE("images", 5, true, List.of(".jpg", ".png", ".jpeg")),

    SYLLABUS("syllabuses", 10, true,
            List.of(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".jpg", ".jpeg", ".png")),

    RECORDING("recordings", 10240, false,

            List.of(".mp4")),

    RESOURCES("resources", 3072, false, List.of()),

    DEMO_VIDEO("demo_videos", 500, true,
            List.of(".mp4"));

    private final String folder;
    private final long maxSizeInMb;
    private final boolean isPublic;
    private final List<String> allowedTypes;
}
