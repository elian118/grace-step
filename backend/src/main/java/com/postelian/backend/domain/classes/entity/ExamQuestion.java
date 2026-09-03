package com.postelian.backend.domain.classes.entity;

import com.postelian.backend.domain.file.entity.File;
import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "exam_questions")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 시험지 제목

    @Column(nullable = false, length = 100)
    private String unit; // 단원 (예: 'to 부정사', '현재진행형')

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question; // 문제

    @Column(nullable = false)
    private String answer; // 정답

    @Column(columnDefinition = "TEXT")
    private String explanation; // 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file; // 파일 메타데이터와 조인

    @Builder
    public ExamQuestion(String title, String unit, String question, String answer, String explanation, File file, String createdBy) {
        this.title = title;
        this.unit = unit;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.file = file;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    // 비즈니스 메서드 (예: 문제 수정)
    public void updateQuestion(String title, String unit, String question, String answer, String explanation, File file, String updatedBy) {
        this.title = title;
        this.unit = unit;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.file = file;
        recordModification(updatedBy);
    }
}
