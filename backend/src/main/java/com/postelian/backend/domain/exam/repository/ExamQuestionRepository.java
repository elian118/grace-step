package com.postelian.backend.domain.exam.repository;

import com.postelian.backend.domain.exam.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    long countByTitle(String title);
    List<ExamQuestion> findByTitle(String title);
}
