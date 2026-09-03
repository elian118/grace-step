package com.postelian.backend.domain.user.entity;

public enum UserStatus {
    PENDING,  // 가입 승인 대기
    ACTIVE,   // 정상 활동 중
    SUSPENDED,// 일시 정지
    WITHDRAWN // 탈퇴
}
