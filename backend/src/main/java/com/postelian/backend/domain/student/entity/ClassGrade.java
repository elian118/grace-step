package com.postelian.backend.domain.student.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClassGrade {
    ELEM("초등반"),
    MIDDLE("중등반"),
    HIGH("고등반");

    private final String description;
}
