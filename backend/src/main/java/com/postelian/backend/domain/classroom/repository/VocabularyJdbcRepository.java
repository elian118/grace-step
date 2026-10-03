package com.postelian.backend.domain.classroom.repository;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VocabularyJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 1,000건 단위 PostgreSQL JDBC Batch Insert
     */
    @Transactional
    public void saveAllBatch(List<Vocabulary> vocabularies) {
        String sql = "INSERT INTO vocabularies " +
                "(original_word, word, past, past_participle, part_of_speech, meaning, example, level, type, note, is_deleted, created_at, updated_at, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false, ?, ?, ?)";

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        jdbcTemplate.batchUpdate(sql, vocabularies, vocabularies.size(), (PreparedStatement ps, Vocabulary v) -> {
            ps.setString(1, v.getOriginalWord());
            ps.setString(2, v.getWord());
            ps.setString(3, v.getPast());
            ps.setString(4, v.getPastParticiple());
            ps.setString(5, v.getPartOfSpeech());
            ps.setString(6, v.getMeaning());
            ps.setString(7, v.getExample());
            ps.setString(8, v.getLevel() != null ? v.getLevel().name() : "NONE");
            ps.setString(9, v.getType() != null ? v.getType().name() : "WORD");
            ps.setString(10, v.getNote());
            ps.setTimestamp(11, now);
            ps.setTimestamp(12, now);
            ps.setString(13, v.getCreatedBy() != null ? v.getCreatedBy() : "system");
        });
    }
}
