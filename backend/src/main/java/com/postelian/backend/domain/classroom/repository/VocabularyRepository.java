package com.postelian.backend.domain.classroom.repository;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    Optional<Vocabulary> findByWord(String word);
}
