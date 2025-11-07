package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${spring.application.storage.public-dir}")
    String publicUploadDir;

    @Value("${spring.application.storage.private-dir}")
    String privateUploadDir;

    public String saveFile(MultipartFile file, ResourceType resourceType) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        final long MAX_SIZE = resourceType.getMaxSizeInMb() * 1024 * 1024;

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 5MB");
        }

        String root = resourceType.isPublic() ? publicUploadDir : privateUploadDir;
        Path uploadPath = Paths.get(root, resourceType.getFolder()).toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Filename is missing");
        }
        String extension = getFileExtension(originalFileName);
        if (!isSupportedExtension(extension, resourceType.getAllowedTypes())) {
            throw new IllegalArgumentException("Unsupported file type; Allowed types: " + resourceType.getAllowedTypes());
        }

        String uniqueFileName = UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(uniqueFileName).normalize();
        file.transferTo(filePath.toFile());

        return uniqueFileName;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }

    private boolean isSupportedExtension(String extension, List<String> allowedTypes) {
        return allowedTypes.contains(extension.toLowerCase());
    }

    @Override
    public byte[] loadFile(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        return Files.readAllBytes(path);
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        Path publicRoot = Paths.get(publicUploadDir).toAbsolutePath().normalize();
        Path privateRoot = Paths.get(privateUploadDir).toAbsolutePath().normalize();

        Path targetPath = Paths.get(filePath).toAbsolutePath().normalize();

        if (!targetPath.startsWith(publicRoot) && !targetPath.startsWith(privateRoot)) {
            throw new IllegalArgumentException("Access denied: File path is outside of storage directories");
        }

        if (!Files.exists(targetPath)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        Files.delete(targetPath);
    }
}
