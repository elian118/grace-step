package com.postelian.backend.domain.classroom.service;

import com.postelian.backend.domain.classroom.dto.VocabularyDto.BatchUploadResult;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyRequest;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyResponse;
import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.repository.VocabularyJdbcRepository;
import com.postelian.backend.domain.classroom.repository.VocabularyRepository;
import com.postelian.backend.domain.classroom.util.ExcelVocabularyBatchUtil;
import com.postelian.backend.global.error.ErrorCode;
import com.postelian.backend.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyJdbcRepository vocabularyJdbcRepository;
    private final ExcelVocabularyBatchUtil excelVocabularyBatchUtil;

    private static final int BATCH_SIZE = 1000; // 1,000건 단위 배치 처리

    /**
     * 엑셀 파일 대량 업로드 (중복 데이터 건너뛰기 적용)
     */
    @Transactional
    public BatchUploadResult uploadVocabulariesFromExcel(MultipartFile file, String createdBy) {
        try {
            // 1. 엑셀 파일 전체 파싱
            List<Vocabulary> parsedList = excelVocabularyBatchUtil.parseExcelToEntities(file.getInputStream(), createdBy);
            int totalParsedCount = parsedList.size();

            if (totalParsedCount == 0) {
                return BatchUploadResult.builder()
                        .totalParsedCount(0)
                        .insertedCount(0)
                        .message("파싱할 수 있는 단어 데이터가 없습니다.")
                        .build();
            }

            // 2. DB에 존재하는 기존 단어 식별자(word + partOfSpeech + meaning)를 Set으로 로드
            Set<String> existingKeys = vocabularyRepository.findAllActiveIdentities().stream()
                    .map(identity -> createKey(identity.getWord(), identity.getPartOfSpeech(), identity.getMeaning()))
                    .collect(Collectors.toSet());

            // 3. 중복 데이터 제거 (기존 DB 데이터 및 엑셀 내부 중복 필터링)
            List<Vocabulary> targetToInsert = parsedList.stream()
                    .filter(v -> {
                        String key = createKey(v.getWord(), v.getPartOfSpeech(), v.getMeaning());
                        if (existingKeys.contains(key)) {
                            return false; // 이미 존재하므로 건너뜀
                        }
                        existingKeys.add(key); // 엑셀 내부 중복 방지를 위해 추가
                        return true;
                    })
                    .collect(Collectors.toList());

            int insertedCount = targetToInsert.size();
            int skippedCount = totalParsedCount - insertedCount;

            // 4. 1,000건 단위 JDBC Batch Insert
            if (insertedCount > 0) {
                for (int i = 0; i < insertedCount; i += BATCH_SIZE) {
                    int end = Math.min(i + BATCH_SIZE, insertedCount);
                    List<Vocabulary> subList = targetToInsert.subList(i, end);
                    vocabularyJdbcRepository.saveAllBatch(subList);
                    log.info("[Batch Insert] {} / {} 건 저장 완료", end, insertedCount);
                }
            }

            return BatchUploadResult.builder()
                    .totalParsedCount(totalParsedCount)
                    .insertedCount(insertedCount)
                    .message(String.format("총 %d건 중 %d건 신규 저장, %d건 중복 건너뜀 완료.", totalParsedCount, insertedCount, skippedCount))
                    .build();

        } catch (Exception e) {
            log.error("엑셀 대량 업로드 중 오류 발생", e);
            throw new RuntimeException("엑셀 파일 업로드 실패: " + e.getMessage());
        }
    }

    private String createKey(String word, String partOfSpeech, String meaning) {
        String p = partOfSpeech != null ? partOfSpeech.trim() : "";
        String m = meaning != null ? meaning.trim() : "";
        return (word.trim() + "||" + p + "||" + m).toLowerCase();
    }

    @Transactional
    public List<VocabularyResponse> registerVocabularies(List<VocabularyRequest> requests, String createdBy) {
        List<Vocabulary> entities = requests.stream()
                .map(req -> Vocabulary.builder()
                        .originalWord(req.getOriginalWord())
                        .word(req.getWord())
                        .past(req.getPast())
                        .pastParticiple(req.getPastParticiple())
                        .partOfSpeech(req.getPartOfSpeech())
                        .meaning(req.getMeaning())
                        .example(req.getExample())
                        .level(req.getLevel())
                        .type(req.getType())
                        .note(req.getNote())
                        .createdBy(createdBy)
                        .build())
                .collect(Collectors.toList());

        return vocabularyRepository.saveAll(entities).stream()
                .map(VocabularyResponse::from)
                .collect(Collectors.toList());
    }

    public List<VocabularyResponse> getAllVocabularies() {
        return vocabularyRepository.findAll().stream()
                .map(VocabularyResponse::from)
                .collect(Collectors.toList());
    }

    public VocabularyResponse getVocabulary(Long id) {
        return vocabularyRepository.findById(id)
                .map(VocabularyResponse::from)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.VOCABULARY_NOT_FOUND));
    }

    @Transactional
    public void deleteVocabularies(List<Long> ids, String deletedBy) {
        List<Vocabulary> vocabularies = vocabularyRepository.findAllById(ids);
        vocabularies.forEach(v -> v.delete(deletedBy));
    }
}
