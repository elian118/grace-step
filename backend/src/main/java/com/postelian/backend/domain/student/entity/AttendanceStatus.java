package com.postelian.backend.domain.student.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {

    ATTENDANCE("출석"),
    LATE("지각"),
    ABSENT("결석"),
    EARLY_LEAVE("조퇴");

    private final String description;
}
