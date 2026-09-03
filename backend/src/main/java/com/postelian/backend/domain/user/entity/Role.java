package com.postelian.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "총괄 관리자"),     // 전체 시스템 관리, 회원 승인 등
    TEACHER("ROLE_TEACHER", "강사/교사"),    // 출석체크, 성적/문법 평가 등록
    STAFF("ROLE_STAFF", "교회 관계자/스태프"), // 출석 조회, 학생 지원 및 관리
    STUDENT("ROLE_STUDENT", "학생/학부모");  // 과제 제출, 개인 성적/출석 조회

    private final String key;
    private final String description;
}
