package com.postelian.backend.domain.word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDefinitionResponseDto {

    private String word;                            // 표제어
    private List<DefinitionItem> definitions;      // 품사별 정의 (뜻, 예문 등)

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefinitionItem {
        private String partOfSpeech; // 품사 (예: 타동사, 명사, 형용사)
        private String meaning;      // 뜻
        private String example;      // 예문
        private String past;         // 과거형 (동사인 경우만 작성, 아니면 null)
        private String pastParticiple; // 과거분사형 (동사인 경우만 작성, 아니면 null)
    }
}
