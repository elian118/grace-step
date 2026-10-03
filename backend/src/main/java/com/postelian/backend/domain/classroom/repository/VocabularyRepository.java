package com.postelian.backend.domain.classroom.repository;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    // word 중복 허용에 따라 List로 조회
    List<Vocabulary> findByWord(String word);

    // 중복 체크용 DTO 인터페이스 (DB 조회 최소화)
    interface WordIdentity {
        String getWord();
        String getPartOfSpeech();
        String getMeaning();
    }

    @Query("SELECT v.word AS word, v.partOfSpeech AS partOfSpeech, v.meaning AS meaning FROM Vocabulary v WHERE v.isDeleted = false")
    List<WordIdentity> findAllActiveIdentities();
}
