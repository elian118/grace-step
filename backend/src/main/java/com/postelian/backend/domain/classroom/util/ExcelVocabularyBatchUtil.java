package com.postelian.backend.domain.classroom.util;

import com.postelian.backend.domain.classroom.entity.Vocabulary;
import com.postelian.backend.domain.classroom.entity.VocabularyLevel;
import com.postelian.backend.domain.classroom.entity.VocabularyType;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelVocabularyBatchUtil {

    private final DataFormatter dataFormatter = new DataFormatter();

    /**
     * 엑셀 파일(InputStream)을 읽어 Vocabulary 엔티티 리스트로 변환
     * A: No, B: 원본목록, C: 표제어, D: 과거, E: 과거분사, F: 품사, G: 뜻, H: 예문, I: 구분, J: 비고
     */
    public List<Vocabulary> parseExcelToEntities(InputStream inputStream, String createdBy) throws Exception {
        List<Vocabulary> vocabularies = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 헤더(0번 행) 제외
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String word = getCellValue(row.getCell(2)); // C열: 표제어
                String meaning = getCellValue(row.getCell(6)); // G열: 뜻

                // C열 표제어와 G열 뜻이 없는 행은 건너뜀
                if (word.isEmpty() || meaning.isEmpty()) continue;

                String originalWord = getCellValue(row.getCell(1)); // B열: 원본목록
                String past = getCellValue(row.getCell(3));         // D열: 과거
                String pastParticiple = getCellValue(row.getCell(4)); // E열: 과거분사
                String partOfSpeech = getCellValue(row.getCell(5));   // F열: 품사
                String example = getCellValue(row.getCell(7));      // H열: 예문
                String levelStr = getCellValue(row.getCell(8));     // I열: 구분
                String note = getCellValue(row.getCell(9));         // J열: 비고

                VocabularyLevel level = VocabularyLevel.fromDescription(levelStr);
                VocabularyType type = VocabularyType.detectType(word);

                Vocabulary vocabulary = Vocabulary.builder()
                        .originalWord(originalWord)
                        .word(word)
                        .past(past.isEmpty() ? null : past)
                        .pastParticiple(pastParticiple.isEmpty() ? null : pastParticiple)
                        .partOfSpeech(partOfSpeech.isEmpty() ? null : partOfSpeech)
                        .meaning(meaning)
                        .example(example.isEmpty() ? null : example)
                        .level(level)
                        .type(type)
                        .note(note.isEmpty() ? null : note)
                        .createdBy(createdBy)
                        .build();

                vocabularies.add(vocabulary);
            }
        }
        return vocabularies;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return dataFormatter.formatCellValue(cell).trim();
    }
}
