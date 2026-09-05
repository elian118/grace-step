package com.postelian.backend.domain.student.dto;

import com.postelian.backend.domain.student.entity.GradeLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class StudentProfileSearchDto {
    private String name;
    private GradeLevel gradeLevel;
    private String schoolName;
    private Boolean isActive;
    private Pageable pageable;
}
