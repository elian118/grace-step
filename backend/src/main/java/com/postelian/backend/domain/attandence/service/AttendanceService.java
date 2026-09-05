package com.postelian.backend.domain.attandence.service;

import com.postelian.backend.domain.attandence.dto.AttendanceDto.AttendanceRequest;
import com.postelian.backend.domain.attandence.dto.AttendanceDto.AttendanceResponse;
import com.postelian.backend.domain.attandence.dto.AttendanceSearchRequestDto;
import com.postelian.backend.domain.attandence.entity.Attendance;
import com.postelian.backend.domain.student.entity.StudentProfile;
import com.postelian.backend.domain.attandence.repository.AttendanceRepository;
import com.postelian.backend.domain.student.repository.StudentProfileRepository;
import com.postelian.backend.global.common.PageMetadata;
import com.postelian.backend.global.common.PageResponse;
import com.postelian.backend.global.error.ErrorCode;
import com.postelian.backend.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentProfileRepository studentProfileRepository;

    /**
     * 1. 출석 기록 및 수정
     * 해당 날짜에 이미 기록이 있으면 수정, 없으면 새로 생성
     */
    @Transactional
    public AttendanceResponse saveOrUpdateAttendance(AttendanceRequest request, String userId) {
        Attendance attendance = attendanceRepository.findByStudentProfileIdAndAttendanceDate(
                        request.getStudentProfileId(), request.getAttendanceDate())
                .map(existing -> {
                    existing.updateAttendance(request.getStatus(), request.getNote(), userId);
                    return existing;
                })
                .orElseGet(() -> {
                    StudentProfile studentProfile = studentProfileRepository.findById(request.getStudentProfileId())
                            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND));
                    return Attendance.builder()
                            .studentProfile(studentProfile)
                            .attendanceDate(request.getAttendanceDate())
                            .status(request.getStatus())
                            .note(request.getNote())
                            .createdBy(userId)
                            .build();
                });

        Attendance saved = attendanceRepository.save(attendance);
        return convertToResponse(saved);
    }

    /**
     * 2. 필터링 기반 출석 목록 조회 (페이지네이션 지원)
     */
    public PageResponse<AttendanceResponse> getAttendanceList(AttendanceSearchRequestDto dto) {
        // 1-based page index adjustment
        Pageable adjustedPageable = PageRequest.of(
                Math.max(0, dto.getPageable().getPageNumber() - 1),
                dto.getPageable().getPageSize(),
                dto.getPageable().getSort()
        );

        Page<Attendance> page = attendanceRepository.findAttendanceList(dto.getStartDate(), dto.getEndDate(), dto.getStudentName(), dto.getStatus(), adjustedPageable);
        return PageResponse.of(
                page.getContent().stream().map(this::convertToResponse).collect(Collectors.toList()),
                PageMetadata.from(page)
        );
    }

    /**
     * 3. 출석 기록 삭제 (Soft Delete)
     */
    @Transactional
    public void deleteAttendance(Long attendanceId, String userId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        attendance.delete(userId);
    }

    private AttendanceResponse convertToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .studentProfileId(attendance.getStudentProfile().getId())
                .studentName(attendance.getStudentProfile().getUser().getName())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .note(attendance.getNote())
                .build();
    }
}
