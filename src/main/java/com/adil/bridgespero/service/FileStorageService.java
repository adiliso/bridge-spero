package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String saveFile(MultipartFile file, ResourceType resourceType);

    byte[] loadFile(String filePath);

    void deleteFile(String filePath);
}
