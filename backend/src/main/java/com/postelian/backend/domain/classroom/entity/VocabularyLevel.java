package com.postelian.backend.domain.classroom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum VocabularyLevel {
    ELEM("초등"),
    MIDDLE_HIGH("중고"),
    ADVANCED("전문"),
    NONE("미지정");

    private final String description;

    public static VocabularyLevel fromDescription(String description) {
        if (description == null || description.isBlank()) {
            return NONE;
        }
        return Arrays.stream(VocabularyLevel.values())
                .filter(level -> level.description.equalsIgnoreCase(description.trim()))
                .findFirst()
                .orElse(NONE);
    }
}
