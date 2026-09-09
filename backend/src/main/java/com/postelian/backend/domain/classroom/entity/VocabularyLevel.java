package com.postelian.backend.domain.classroom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VocabularyLevel {
    ELEM("초등"),
    MIDDLE_HIGH("중고"),
    ADVANCED("전문");

    private final String description;
}
