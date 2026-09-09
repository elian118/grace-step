package com.postelian.backend.domain.classroom;

import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyRequest;
import com.postelian.backend.domain.classroom.dto.VocabularyDto.VocabularyResponse;
import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.entity.VocabularyLevel;
import com.postelian.backend.domain.classroom.entity.VocabularyType;
import com.postelian.backend.domain.classroom.repository.VocabularyRepository;
import com.postelian.backend.domain.classroom.service.VocabularyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularyServiceTest {

    @Mock
    private VocabularyRepository vocabularyRepository;

    @InjectMocks
    private VocabularyService vocabularyService;

    @Test
    void registerVocabularies_ShouldSaveAndReturnList() {
        // given
        VocabularyRequest request = VocabularyRequest.builder()
                .word("Test")
                .meaning("테스트")
                .level(VocabularyLevel.ELEM)
                .type(VocabularyType.WORD)
                .build();
        when(vocabularyRepository.save(any(Vocabulary.class))).thenAnswer(i -> i.getArgument(0));

        // when
        List<VocabularyResponse> responses = vocabularyService.registerVocabularies(List.of(request), "tester");

        // then
        assertEquals(1, responses.size());
        assertEquals("Test", responses.getFirst().getWord());
        assertEquals(VocabularyType.WORD, responses.getFirst().getType());
        verify(vocabularyRepository, times(1)).save(any(Vocabulary.class));
    }

    @Test
    void upsertVocabulary_ShouldUpdateIfExist() {
        // given
        Vocabulary existing = Vocabulary.builder().word("Test").meaning("기존").level(VocabularyLevel.ELEM).type(VocabularyType.WORD).build();
        when(vocabularyRepository.findByWord("Test")).thenReturn(Optional.of(existing));
        VocabularyRequest request = VocabularyRequest.builder()
                .word("Test")
                .meaning("수정")
                .level(VocabularyLevel.MIDDLE_HIGH)
                .type(VocabularyType.IDIOM)
                .build();

        // when
        VocabularyResponse response = vocabularyService.upsertVocabulary(request, "tester");

        // then
        assertEquals("수정", response.getMeaning());
        assertEquals(VocabularyLevel.MIDDLE_HIGH, response.getLevel());
        assertEquals(VocabularyType.IDIOM, response.getType());
    }
}
