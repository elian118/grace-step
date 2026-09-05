package com.postelian.backend.domain.exam.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class AiExamResponseDto {
    private List<QuestionDto> questions;

    @Getter
    public static class QuestionDto {
        private String question;
        private String answer;
        private String explanation;
    }
}
