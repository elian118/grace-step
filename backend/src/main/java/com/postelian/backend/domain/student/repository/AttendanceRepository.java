package com.postelian.backend.domain.student.repository;

import com.postelian.backend.domain.student.entity.Attendance;
import com.postelian.backend.domain.student.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 학생 ID와 날짜로 특정 출석 기록 조회 (기록 수정 시 사용)
    Optional<Attendance> findByStudentProfileIdAndAttendanceDate(Long studentProfileId, LocalDate attendanceDate);

    /**
     * 필터링 기반 출석 목록 조회
     * - startDate, endDate: 필구 (기간 조회)
     * - studentName: 부분 일치 (Optional)
     * - status: 일치 (Optional)
     */
    @Query("SELECT a FROM Attendance a " +
           "JOIN FETCH a.studentProfile sp " +
           "JOIN FETCH sp.user u " +
           "WHERE a.attendanceDate BETWEEN :startDate AND :endDate " +
           "AND (:studentName IS NULL OR u.name LIKE %:studentName%) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "ORDER BY a.attendanceDate DESC, u.name ASC")
    List<Attendance> findAttendanceList(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("studentName") String studentName,
            @Param("status") AttendanceStatus status
    );
}
