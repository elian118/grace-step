package com.postelian.backend.domain.student.dto;

import com.postelian.backend.domain.student.entity.GradeLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileSearchDto {
    private String name;
    private GradeLevel gradeLevel;
    private String schoolName;
    private Boolean isActive;
    private int page = 1;
    private int size = 10;
    private String[] sort;
}
