package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileStorageService {

    String savePublicFile(MultipartFile file, ResourceType resourceType) throws IOException;

    String savePrivateFile(MultipartFile file, ResourceType resourceType) throws IOException;

    byte[] loadPrivateFile(String filePath) throws IOException;
}
