package com.adil.bridgespero.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {

    String savePublicFile(MultipartFile file, String folder);
    String savePrivateFile(MultipartFile file, String folder);
    byte[] loadPrivateFile(String filename, String folder);
}
