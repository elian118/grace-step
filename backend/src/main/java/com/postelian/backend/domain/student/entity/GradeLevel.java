package com.postelian.backend.domain.student.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GradeLevel {

    ELEM_1("초1"),
    ELEM_2("초2"),
    ELEM_3("초3"),
    ELEM_4("초4"),
    ELEM_5("초5"),
    ELEM_6("초6"),
    MIDDLE_1("중1"),
    MIDDLE_2("중2"),
    MIDDLE_3("중3"),
    HIGH_1("고1"),
    HIGH_2("고2"),
    HIGH_3("고3");

    private final String description;
}
