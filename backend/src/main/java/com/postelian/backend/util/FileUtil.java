package com.postelian.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUtil {

    @Value("${FILE_UPLOAD_DIR}")
    private String fileUploadDir;

    // 1. 파일 저장
    public String saveFile(String type, String fileFolderKey, MultipartFile file) throws IOException {
        return saveBytes(type, fileFolderKey, file.getOriginalFilename(), file.getInputStream());
    }

    public String saveBytes(String type, String fileFolderKey, String fileName, InputStream inputStream) throws IOException {
        Path uploadPath = Paths.get(fileUploadDir, type, fileFolderKey);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filePath.toString();
    }

    // 2. 파일 조회
    public Resource loadFile(String type, String fileFolderKey, String fileName) throws IOException {
        Path filePath = Paths.get(fileUploadDir, type, fileFolderKey).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("파일을 찾을 수 없습니다: " + fileName);
        }
    }

    // 3. 파일 삭제
    public void deleteFile(String type, String fileFolderKey, String fileName) throws IOException {
        Path filePath = Paths.get(fileUploadDir, type, fileFolderKey).resolve(fileName);
        Files.deleteIfExists(filePath);
    }
}
