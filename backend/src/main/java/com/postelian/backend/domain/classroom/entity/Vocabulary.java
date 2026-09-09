package com.postelian.backend.domain.classroom.entity;

import com.postelian.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "vocabularies", indexes = {
        @Index(name = "idx_vocabulary_word", columnList = "word", unique = true)
})
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(nullable = false, length = 255)
    private String meaning;

    @Column(length = 500)
    private String example;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VocabularyLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VocabularyType type;

    @Builder
    public Vocabulary(String word, String meaning, String example, VocabularyLevel level, VocabularyType type, String createdBy) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
        this.type = type;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    public void update(String meaning, String example, VocabularyLevel level, VocabularyType type, String updatedBy) {
        this.meaning = meaning;
        this.example = example;
        this.level = level;
        this.type = type;
        recordModification(updatedBy);
    }
}
