package com.postelian.backend.domain.classroom.dto;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.entity.VocabularyLevel;
import com.postelian.backend.domain.classroom.entity.VocabularyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VocabularyDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "영단어 등록/수정 요청 DTO")
    public static class VocabularyRequest {

        @Schema(description = "원본 목록", example = "abandon")
        private String originalWord;

        @NotBlank
        @Schema(description = "표제어", example = "abandon")
        private String word;

        @Schema(description = "과거형", example = "abandoned")
        private String past;

        @Schema(description = "과거분사형", example = "abandoned")
        private String pastParticiple;

        @Schema(description = "품사", example = "타동사")
        private String partOfSpeech;

        @NotBlank
        @Schema(description = "의미", example = "버리다, 포기하다")
        private String meaning;

        @Schema(description = "예문", example = "He had to abandon his car.")
        private String example;

        @Schema(description = "레벨", example = "ELEM")
        private VocabularyLevel level;

        @Schema(description = "구분(단어/숙어)", example = "WORD")
        private VocabularyType type;

        @Schema(description = "비고", example = "")
        private String note;

        @Builder
        public VocabularyRequest(String originalWord, String word, String past, String pastParticiple,
                                 String partOfSpeech, String meaning, String example,
                                 VocabularyLevel level, VocabularyType type, String note) {
            this.originalWord = originalWord;
            this.word = word;
            this.past = past;
            this.pastParticiple = pastParticiple;
            this.partOfSpeech = partOfSpeech;
            this.meaning = meaning;
            this.example = example;
            this.level = level;
            this.type = type;
            this.note = note;
        }
    }

    @Getter
    @Schema(description = "영단어 응답 DTO")
    public static class VocabularyResponse {

        private final Long id;
        private final String originalWord;
        private final String word;
        private final String past;
        private final String pastParticiple;
        private final String partOfSpeech;
        private final String meaning;
        private final String example;
        private final VocabularyLevel level;
        private final VocabularyType type;
        private final String note;

        public VocabularyResponse(Vocabulary vocabulary) {
            this.id = vocabulary.getId();
            this.originalWord = vocabulary.getOriginalWord();
            this.word = vocabulary.getWord();
            this.past = vocabulary.getPast();
            this.pastParticiple = vocabulary.getPastParticiple();
            this.partOfSpeech = vocabulary.getPartOfSpeech();
            this.meaning = vocabulary.getMeaning();
            this.example = vocabulary.getExample();
            this.level = vocabulary.getLevel();
            this.type = vocabulary.getType();
            this.note = vocabulary.getNote();
        }

        public static VocabularyResponse from(Vocabulary vocabulary) {
            return new VocabularyResponse(vocabulary);
        }
    }

    @Getter
    @Builder
    @Schema(description = "엑셀 대량 업로드 결과 DTO")
    public static class BatchUploadResult {
        private int totalParsedCount;
        private int insertedCount;
        private String message;
    }
}
