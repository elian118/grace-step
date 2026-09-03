package com.postelian.backend.domain.student.repository;

import com.postelian.backend.domain.student.entity.GradeLevel;
import com.postelian.backend.domain.student.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    @Query("SELECT sp FROM StudentProfile sp JOIN sp.user u WHERE " +
           "(:name IS NULL OR u.name LIKE %:name%) AND " +
           "(:gradeLevel IS NULL OR sp.gradeLevel = :gradeLevel) AND " +
           "(:schoolName IS NULL OR sp.schoolName LIKE %:schoolName%) AND " +
           "(:isActive IS NULL OR sp.isActive = :isActive)")
    Page<StudentProfile> search(
            @Param("name") String name,
            @Param("gradeLevel") GradeLevel gradeLevel,
            @Param("schoolName") String schoolName,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
}
