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
        @Index(name = "idx_vocabulary_word", columnList = "word") // Unique 제약조건 제거
})
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_word", length = 150)
    private String originalWord; // B열: 원본 목록 (표제어+파생어)

    @Column(nullable = false, length = 100)
    private String word; // C열: 표제어

    @Column(length = 100)
    private String past; // D열: 과거

    @Column(name = "past_participle", length = 100)
    private String pastParticiple; // E열: 과거분사

    @Column(name = "part_of_speech", length = 50)
    private String partOfSpeech; // F열: 품사

    @Column(nullable = false, length = 500)
    private String meaning; // G열: 뜻

    @Column(columnDefinition = "TEXT")
    private String example; // H열: 예문

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VocabularyLevel level; // I열: 구분 (초등, 중고 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VocabularyType type; // 단어 / 숙어 판별

    @Column(length = 255)
    private String note; // J열: 비고

    @Builder
    public Vocabulary(String originalWord, String word, String past, String pastParticiple,
                      String partOfSpeech, String meaning, String example,
                      VocabularyLevel level, VocabularyType type, String note, String createdBy) {
        this.originalWord = originalWord;
        this.word = word;
        this.past = past;
        this.pastParticiple = pastParticiple;
        this.partOfSpeech = partOfSpeech;
        this.meaning = meaning;
        this.example = example;
        this.level = level != null ? level : VocabularyLevel.NONE;
        this.type = type != null ? type : VocabularyType.WORD;
        this.note = note;
        if (createdBy != null) {
            recordCreation(createdBy);
        }
    }

    public void update(String originalWord, String past, String pastParticiple, String partOfSpeech,
                       String meaning, String example, VocabularyLevel level, VocabularyType type, String note, String updatedBy) {
        this.originalWord = originalWord;
        this.past = past;
        this.pastParticiple = pastParticiple;
        this.partOfSpeech = partOfSpeech;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
        this.type = type;
        this.note = note;
        recordModification(updatedBy);
    }
}
