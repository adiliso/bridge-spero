package com.adil.bridgespero.service;

import com.adil.bridgespero.domain.model.enums.ResourceType;
import com.adil.bridgespero.exception.BaseException;
import com.adil.bridgespero.exception.IllegalArgumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.adil.bridgespero.domain.model.enums.ErrorCode.INTERNAL_ERROR;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${spring.application.storage.public-dir}")
    String publicUploadDir;

    @Value("${spring.application.storage.private-dir}")
    String privateUploadDir;

    @Override
    public String saveFile(MultipartFile file, ResourceType resourceType) {
        String fileName;
        try {
            fileName = save(file, resourceType);
        } catch (IOException e) {
            throw new BaseException("Something went wrong. Try again", INTERNAL_ERROR);
        }
        return fileName;
    }

    private String save(MultipartFile file, ResourceType resourceType) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        final long MAX_SIZE = resourceType.getMaxSizeInMb() * 1024 * 1024;

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of " + resourceType.getMaxSizeInMb() + " mb");
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

        return "/" + (resourceType.isPublic() ? "public" : "private")
               + "/" + resourceType.getFolder()
               + "/" + uniqueFileName;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }

    private boolean isSupportedExtension(String extension, List<String> allowedTypes) {
        if (allowedTypes.isEmpty()) return true;
        return allowedTypes.contains(extension.toLowerCase());
    }

    @Override
    public Resource load(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        Path targetPath = getTargetPath(filePath);

        try {
            return new UrlResource(targetPath.toUri());
        } catch (MalformedURLException e) {
            throw new BaseException("Failed to load resource", INTERNAL_ERROR);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        Path publicRoot = Paths.get(publicUploadDir).toAbsolutePath().normalize();
        Path privateRoot = Paths.get(privateUploadDir).toAbsolutePath().normalize();

        Path targetPath = getTargetPath(filePath);

        if (!targetPath.startsWith(publicRoot) && !targetPath.startsWith(privateRoot)) {
            throw new IllegalArgumentException("Access denied: File path is outside of storage directories");
        }

        try {
            Files.delete(targetPath);
        } catch (IOException e) {
            throw new BaseException("Something went wrong. Try again", INTERNAL_ERROR);
        }
    }

    @Override
    public String probeContentType(String filePath) {
        Path targetPath = getTargetPath(filePath);

        try {
            return Files.probeContentType(targetPath);
        } catch (IOException e) {
            throw new BaseException("Internal error", INTERNAL_ERROR);
        }
    }

    private Path getTargetPath(String filePath) {
        Path targetPath;
        if (filePath.startsWith("/public/")) {
            String relative = filePath.substring("/public/".length());
            targetPath = Paths.get(publicUploadDir, relative).toAbsolutePath().normalize();
        } else if (filePath.startsWith("/private/")) {
            String relative = filePath.substring("/private/".length());
            targetPath = Paths.get(privateUploadDir, relative).toAbsolutePath().normalize();
        } else {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        if (!Files.exists(targetPath)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        return targetPath;
    }
}
