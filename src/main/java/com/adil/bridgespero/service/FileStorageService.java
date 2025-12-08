package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveFile(MultipartFile file, ResourceType resourceType);

    Resource load(String filePath);

    void deleteFile(String filePath);

    String probeContentType(String filePath);
}
