package com.postelian.backend.domain.word.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postelian.backend.domain.word.dto.WordDefinitionResponseDto;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WordGeminiAiUtil {

    private final GoogleAiGeminiChatModel chatModel;
    private final ObjectMapper objectMapper;

    private String promptTemplate;
    private static final int MAX_RETRY = 2; // 파싱 실패 시 최대 재시도 횟수

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/word_definition_prompt.md");
            try (InputStream inputStream = resource.getInputStream()) {
                this.promptTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("프롬프트 md 파일 읽기 실패. 기본 템플릿으로 대체합니다.", e);
            this.promptTemplate = "다음 단어 목록 {wordList} 에 대해 정해진 JSON 형식으로 응답하세요.";
        }
    }

    /**
     * 단어 리스트를 받아 Gemini API 호출 및 파싱 (실패 시 자동 재시도)
     */
    public List<WordDefinitionResponseDto> generateWordDefinitions(List<String> words) {
        if (words == null || words.isEmpty()) {
            return List.of();
        }

        String wordListString = String.join(", ", words);
        String finalPrompt = this.promptTemplate.replace("{wordList}", wordListString);

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                String response = chatModel.generate(finalPrompt);

                // 1. JSON 배열 추출 및 전처리
                String jsonOnly = extractJsonArray(response);

                // 2. Jackson JSON 파싱
                return objectMapper.readValue(jsonOnly, new TypeReference<>() {
                });

            } catch (Exception e) {
                log.warn("[시도 {}/{}] Gemini JSON 파싱 실패 (대상 단어: {}). 사유: {}",
                        attempt, MAX_RETRY, words.getFirst() + "...", e.getMessage());

                if (attempt == MAX_RETRY) {
                    log.error("Gemini API 재시도 횟수 초과. 해당 배치는 건너뜁니다. 단어: {}", words, e);
                }
            }
        }

        return List.of();
    }

    /**
     * 응답 문자열에서 순수 JSON 배열만 안전하게 추출하는 메서드
     */
    private String extractJsonArray(String rawResponse) {
        if (rawResponse == null) return "[]";

        // 마크다운 블록 제거
        String cleaned = rawResponse
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("(?s)```\\s*", "")
                .trim();

        // 첫번째 '[' 와 마지막 ']' 위치 탐색
        int firstBracket = cleaned.indexOf('[');
        int lastBracket = cleaned.lastIndexOf(']');

        if (firstBracket != -1 && lastBracket != -1 && firstBracket < lastBracket) {
            cleaned = cleaned.substring(firstBracket, lastBracket + 1);
        }

        return cleaned;
    }
}
