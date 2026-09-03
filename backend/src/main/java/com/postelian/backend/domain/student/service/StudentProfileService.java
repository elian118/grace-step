package com.postelian.backend.domain.student.service;

import com.postelian.backend.domain.student.dto.StudentProfileDto.StudentProfileRequest;
import com.postelian.backend.domain.student.dto.StudentProfileDto.StudentProfileResponse;
import com.postelian.backend.domain.student.entity.GradeLevel;
import com.postelian.backend.domain.student.entity.StudentProfile;
import com.postelian.backend.domain.student.repository.StudentProfileRepository;
import com.postelian.backend.domain.user.entity.User;
import com.postelian.backend.domain.user.repository.UserRepository;
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
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    /**
     * 학생 프로필 다건 조회 (페이지네이션 및 필터링 검색)
     */
    public PageResponse<StudentProfileResponse> getStudentProfileList(String name, GradeLevel gradeLevel, String schoolName, Boolean isActive, Pageable pageable) {
        // 1-based page index adjustment
        Pageable adjustedPageable = PageRequest.of(
                Math.max(0, pageable.getPageNumber() - 1),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<StudentProfile> page = studentProfileRepository.search(name, gradeLevel, schoolName, isActive, adjustedPageable);
        return PageResponse.of(
                page.getContent().stream().map(StudentProfileResponse::from).collect(Collectors.toList()),
                PageMetadata.from(page)
        );
    }

    /**
     * 학생 프로필 등록
     */
    @Transactional
    public StudentProfileResponse registerStudent(StudentProfileRequest request, String createdBy) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        StudentProfile profile = StudentProfile.builder()
                .user(user)
                .teacher(teacher)
                .schoolName(request.getSchoolName())
                .gradeLevel(request.getGradeLevel())
                .parentPhoneNumber(request.getParentPhoneNumber())
                .memo(request.getMemo())
                .createdBy(createdBy)
                .build();

        StudentProfile savedProfile = studentProfileRepository.save(profile);
        return StudentProfileResponse.from(savedProfile);
    }

    /**
     * 학생 프로필 수정
     */
    @Transactional
    public StudentProfileResponse updateStudent(Long id, StudentProfileRequest request, String updatedBy) {
        StudentProfile profile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        // 담당 강사 변경 필요 시
        if (!profile.getTeacher().getId().equals(request.getTeacherId())) {
             User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
             profile.assignTeacher(teacher, updatedBy);
        }

        profile.updateProfile(request.getSchoolName(), request.getGradeLevel(),
                request.getParentPhoneNumber(), request.getMemo(), updatedBy);

        return StudentProfileResponse.from(profile);
    }

    /**
     * 학생 프로필 삭제 (Soft Delete)
     */
    @Transactional
    public void deleteStudent(Long id, String deletedBy) {
        StudentProfile profile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.STUDENT_NOT_FOUND));

        profile.delete(deletedBy);
    }
}
