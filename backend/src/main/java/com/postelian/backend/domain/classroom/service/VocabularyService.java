package com.postelian.backend.domain.classroom.service;

import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyRequest;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyResponse;
import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.repository.VocabularyRepository;
import com.postelian.backend.global.error.ErrorCode;
import com.postelian.backend.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    @Transactional
    public List<VocabularyResponse> registerVocabularies(List<VocabularyRequest> requests, String createdBy) {
        return requests.stream()
                .map(request -> vocabularyRepository.save(Vocabulary.builder()
                        .word(request.getWord())
                        .meaning(request.getMeaning())
                        .example(request.getExample())
                        .level(request.getLevel())
                        .type(request.getType())
                        .createdBy(createdBy)
                        .build()))
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
    public VocabularyResponse upsertVocabulary(VocabularyRequest request, String updatedBy) {
        Vocabulary vocabulary = vocabularyRepository.findByWord(request.getWord())
                .map(v -> {
                    v.update(request.getMeaning(), request.getExample(), request.getLevel(), request.getType(), updatedBy);
                    return v;
                })
                .orElseGet(() -> vocabularyRepository.save(Vocabulary.builder()
                        .word(request.getWord())
                        .meaning(request.getMeaning())
                        .example(request.getExample())
                        .level(request.getLevel())
                        .type(request.getType())
                        .createdBy(updatedBy)
                        .build()));
        return VocabularyResponse.from(vocabulary);
    }

    @Transactional
    public void deleteVocabularies(List<Long> ids, String deletedBy) {
        List<Vocabulary> vocabularies = vocabularyRepository.findAllById(ids);
        vocabularies.forEach(v -> v.delete(deletedBy));
    }
}
