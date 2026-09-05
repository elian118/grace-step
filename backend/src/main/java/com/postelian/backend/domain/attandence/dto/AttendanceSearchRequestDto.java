package com.postelian.backend.domain.attandence.dto;

import com.postelian.backend.domain.attandence.entity.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceSearchRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String studentName;
    private AttendanceStatus status;
    private Pageable pageable;
}
