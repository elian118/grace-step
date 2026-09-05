package com.postelian.backend.domain.common.file.service;

import com.postelian.backend.config.properties.FileUploadProperties;
import com.postelian.backend.domain.common.file.dto.FileResponseDto;
import com.postelian.backend.domain.common.file.dto.FileUploadRequestDto;
import com.postelian.backend.domain.common.file.entity.File;
import com.postelian.backend.domain.common.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final FileUploadProperties fileUploadProperties;

    /**
     * 파일 단건 업로드
     */
    @Transactional
    public FileResponseDto uploadFile(FileUploadRequestDto.Single dto, String createdBy) {
        if (dto.getFile().isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        String targetDir = getTargetDirectory(dto.getType());
        Path uploadPath = Paths.get(targetDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = dto.getFile().getOriginalFilename();
            String storeFilename = createStoreFileName(originalFilename);
            Path filePath = uploadPath.resolve(storeFilename);

            dto.getFile().transferTo(filePath.toFile());

            File fileEntity = File.builder()
                    .fileFolderKey(dto.getFileFolderKey())
                    .type(dto.getType())
                    .originalFilename(originalFilename)
                    .storedFilename(storeFilename)
                    .filePath(filePath.toString())
                    .fileSize(dto.getFile().getSize())
                    .createdBy(createdBy)
                    .build();

            File savedFile = fileRepository.save(fileEntity);
            return FileResponseDto.from(savedFile);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파일 다건 업로드
     */
    @Transactional
    public List<FileResponseDto> uploadFiles(FileUploadRequestDto.Multiple request, String createdBy) {
        List<FileResponseDto> result = new ArrayList<>();
        if (request.getFiles() != null) {
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    FileUploadRequestDto.Single singleRequest = new FileUploadRequestDto.Single();
                    singleRequest.setFile(file);
                    singleRequest.setFileFolderKey(request.getFileFolderKey());
                    singleRequest.setType(request.getType());

                    result.add(uploadFile(singleRequest, createdBy));
                }
            }
        }
        return result;
    }

    /**
     * 파일 다운로드
     */
    public Resource downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다. ID: " + fileId));

        try {
            Path path = Paths.get(file.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 읽을 수 없거나 찾을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 경로가 유효하지 않습니다.", e);
        }
    }

    /**
     * 파일 entity 정보 조회
     */
    public File getFileEntity(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다. ID: " + fileId));
    }

    /**
     * 타입과 생성일 범위로 파일 조회
     */
    public List<FileResponseDto> getFileEntitiesByTypeAndDate(String type, LocalDateTime startDate, LocalDateTime endDate) {
        return fileRepository.findByTypeAndCreatedAtBetween(type, startDate, endDate)
                .stream()
                .map(FileResponseDto::from)
                .toList();
    }

    /**
     * 파일 삭제 (물리 파일 삭제 및 DB Soft Delete)
     */
    @Transactional
    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다. ID: " + fileId));

        Path path = Paths.get(file.getFilePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("물리 파일 삭제 중 오류가 발생했습니다.", e);
        }

        fileRepository.delete(file);
    }

    // 타입에 맞춰 저장 기본 경로 선택
    private String getTargetDirectory(String type) {
        if ("EXAM".equalsIgnoreCase(type)) {
            return fileUploadProperties.getSubDir().getExams();
        } else if ("ATTENDANCE".equalsIgnoreCase(type)) {
            return fileUploadProperties.getSubDir().getAttendances();
        }
        return fileUploadProperties.getBaseDir();
    }

    // 서버 저장용 고유 파일명 생성
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return (pos != -1) ? originalFilename.substring(pos + 1) : "";
    }
}
