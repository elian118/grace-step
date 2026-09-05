package com.postelian.backend.domain.exam.util;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GeminiAiUtil {

    private final GoogleAiGeminiChatModel chatModel;

    public String generateResponse(String promptFilePath, com.postelian.backend.domain.exam.dto.ExamRequestDto dto) throws IOException {
        String promptTemplate = loadPromptTemplate(promptFilePath);

        String finalPrompt = promptTemplate
                .replace("{startGrade}", dto.getStartGrade())
                .replace("{endGrade}", dto.getEndGrade())
                .replace("{unit}", dto.getUnit())
                .replace("{examTime}", String.valueOf(dto.getExamTime()))
                .replace("{questionCount}", String.valueOf(dto.getQuestionCount()));

        return chatModel.generate(finalPrompt);
    }

    private String loadPromptTemplate(String promptFilePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(promptFilePath);
        return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
    }
}
