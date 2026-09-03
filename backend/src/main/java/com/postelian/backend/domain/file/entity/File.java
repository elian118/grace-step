package com.postelian.backend.domain.file.entity;

import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "files", indexes = {
        @Index(name = "idx_file_folder_key", columnList = "file_folder_key")
})
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_folder_key", nullable = false)
    private String fileFolderKey; // 경로 유추를 위한 폴더 구분 키

    @Column(nullable = false)
    private String type; // 파일 목적/유형 (e.g., "STUDENT_PROFILE", "ATTENDANCE_DOC")

    @Column(name = "original_filename", nullable = false)
    private String originalFilename; // 사용자가 업로드한 원본 이름

    @Column(name = "stored_filename", nullable = false)
    private String storedFilename; // 실제 서버에 저장된 파일 이름

    @Column(nullable = false)
    private String filePath; // 저장된 전체 경로

    private Long fileSize; // 바이트 단위 파일 크기

    @Builder
    public File(String fileFolderKey, String type, String originalFilename, String storedFilename, 
                String filePath, Long fileSize, String createdBy) {
        this.fileFolderKey = fileFolderKey;
        this.type = type;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }
}
