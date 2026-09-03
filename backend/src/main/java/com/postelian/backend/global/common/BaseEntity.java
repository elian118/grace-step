package com.postelian.backend.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    private String deletedBy;

    // 등록자/수정자 기록용 도메인 메서드
    public void recordCreation(String createdBy) {
        this.createdBy = createdBy;
        this.updatedBy = createdBy; // 등록 시점에는 수정자도 동일하게 지정
    }

    public void recordModification(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Soft Delete 처리를 위한 도메인 메서드
    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
