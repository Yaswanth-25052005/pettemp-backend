package com.example.petadoption.user.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for saving uploaded pet images.
 * Files are stored to /uploads directory in project root.
 */
@Component
public class FileStorageUtil {

    private final Path uploadsDir = Paths.get("uploads");

    public FileStorageUtil() throws IOException {
        if (!Files.exists(uploadsDir)) {
            Files.createDirectories(uploadsDir);
        }
    }

    public List<String> saveFiles(MultipartFile[] files) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        if (files == null) return fileUrls;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String fileName = System.currentTimeMillis() + "_" +
                    file.getOriginalFilename().replaceAll("\\s+", "_");

            Path filePath = uploadsDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // public URL path
            fileUrls.add("/uploads/" + fileName);
        }
        return fileUrls;
    }
}
