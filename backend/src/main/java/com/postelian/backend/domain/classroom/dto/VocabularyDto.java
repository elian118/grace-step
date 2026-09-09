package com.postelian.backend.domain.classroom.dto;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.entity.VocabularyLevel;
import com.postelian.backend.domain.classroom.entity.VocabularyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @NotBlank
        @Schema(description = "단어 또는 숙어", example = "Apple")
        private String word;

        @NotBlank
        @Schema(description = "의미", example = "사과")
        private String meaning;

        @Schema(description = "예문", example = "I ate an apple.")
        private String example;

        @NotNull
        @Schema(description = "레벨", example = "ELEM")
        private VocabularyLevel level;

        @NotNull
        @Schema(description = "구분(단어/숙어)", example = "WORD")
        private VocabularyType type;

        @Builder
        public VocabularyRequest(String word, String meaning, String example, VocabularyLevel level, VocabularyType type) {
            this.word = word;
            this.meaning = meaning;
            this.example = example;
            this.level = level;
            this.type = type;
        }
    }

    @Getter
    @Schema(description = "영단어 응답 DTO")
    public static class VocabularyResponse {

        @Schema(description = "ID")
        private final Long id;
        @Schema(description = "단어 또는 숙어")
        private final String word;
        @Schema(description = "의미")
        private final String meaning;
        @Schema(description = "예문")
        private final String example;
        @Schema(description = "레벨")
        private final VocabularyLevel level;
        @Schema(description = "구분(단어/숙어)")
        private final VocabularyType type;

        public VocabularyResponse(Vocabulary vocabulary) {
            this.id = vocabulary.getId();
            this.word = vocabulary.getWord();
            this.meaning = vocabulary.getMeaning();
            this.example = vocabulary.getExample();
            this.level = vocabulary.getLevel();
            this.type = vocabulary.getType();
        }

        public static VocabularyResponse from(Vocabulary vocabulary) {
            return new VocabularyResponse(vocabulary);
        }
    }
}
