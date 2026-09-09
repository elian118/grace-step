package com.postelian.backend.domain.classroom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VocabularyType {
    WORD("단어"),
    IDIOM("숙어");

    private final String description;
}
