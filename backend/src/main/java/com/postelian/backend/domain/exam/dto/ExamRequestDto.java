package com.postelian.backend.domain.exam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "시험지 생성 요청 DTO")
public class ExamRequestDto {

    @Schema(description = "제목 (선택)", example = "to 부정사 기초 테스트")
    private String title;

    @Schema(description = "시작 학년", example = "초5")
    private String startGrade;

    @Schema(description = "종료 학년", example = "중1")
    private String endGrade;

    @Schema(description = "단원", example = "to 부정사")
    private String unit;

    @Schema(description = "시험 시간(분)", example = "30")
    private Integer examTime;

    @Schema(description = "문항 수", example = "10")
    private Integer questionCount;
}
