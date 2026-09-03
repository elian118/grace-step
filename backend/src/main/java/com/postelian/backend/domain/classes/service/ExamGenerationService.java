package com.postelian.backend.domain.classes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postelian.backend.domain.classes.dto.AiExamResponseDto;
import com.postelian.backend.domain.classes.dto.ExamRequestDto;
import com.postelian.backend.domain.classes.entity.ExamQuestion;
import com.postelian.backend.domain.classes.repository.ExamQuestionRepository;
import com.postelian.backend.domain.classes.util.GeminiAiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamGenerationService {

    private final GeminiAiUtil geminiAiUtil;
    private final ExamQuestionRepository examQuestionRepository;
    private final ObjectMapper objectMapper;

    private static final String PROMPT_PATH = "com/postelian/backend/domain/classes/prompts/exam_generation_prompt.md";

    @Transactional
    public List<ExamQuestion> generateAndSaveExam(ExamRequestDto dto, String userId) throws IOException {
        String aiResponse = geminiAiUtil.generateResponse(PROMPT_PATH, dto);
        AiExamResponseDto responseDto = objectMapper.readValue(aiResponse, AiExamResponseDto.class);

        String title = determineTitle(dto);

        List<ExamQuestion> questions = responseDto.getQuestions().stream()
                .map(q -> ExamQuestion.builder()
                        .title(title)
                        .unit(dto.getUnit())
                        .question(q.getQuestion())
                        .answer(q.getAnswer())
                        .explanation(q.getExplanation())
                        .createdBy(userId)
                        .build())
                .collect(Collectors.toList());

        return examQuestionRepository.saveAll(questions);
    }

    private String determineTitle(ExamRequestDto dto) {
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            return dto.getTitle();
        }
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = examQuestionRepository.countByTitle(dto.getUnit() + "_" + date);
        return dto.getUnit() + "_" + date + "_" + (count + 1);
    }
}
