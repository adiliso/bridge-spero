package com.adil.bridgespero.domain.model.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record SyllabusCreateRequest(

        MultipartFile file
) {
}
