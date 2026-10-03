package com.postelian.backend.domain.classroom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VocabularyType {
    WORD("단어"),
    IDIOM("숙어");

    private final String description;

    public static VocabularyType detectType(String word) {
        if (word != null && word.trim().contains(" ")) {
            return IDIOM;
        }
        return WORD;
    }
}
