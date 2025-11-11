package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String saveFile(MultipartFile file, ResourceType resourceType) throws IOException;

    byte[] loadFile(String filePath) throws IOException;

    void deleteFile(String filePath) throws IOException;
}
