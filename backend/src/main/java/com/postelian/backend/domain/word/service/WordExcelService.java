package com.postelian.backend.domain.word.service;

import com.postelian.backend.domain.word.dto.WordDefinitionResponseDto;
import com.postelian.backend.domain.word.util.ExcelWordProcessUtil;
import com.postelian.backend.domain.word.util.WordGeminiAiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordExcelService {

    private final ExcelWordProcessUtil excelUtil;
    private final WordGeminiAiUtil geminiAiUtil;

    private static final int BATCH_SIZE = 20;

    public byte[] processAndFillExcel(InputStream inputStream) throws Exception {
        // 1. 임시 작업 파일 생성
        File tempFile = File.createTempFile("word_process_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            inputStream.transferTo(fos);
        }

        try {
            // 2. 미완성된(비어있는) 단어만 추출
            List<String> targetWords = excelUtil.extractTargetWords(tempFile);
            int totalWords = targetWords.size();

            if (totalWords == 0) {
                log.info("채워야 할 비어있는 단어가 없습니다. 기존 파일을 반환합니다.");
                return Files.readAllBytes(tempFile.toPath());
            }

            int totalBatches = (int) Math.ceil((double) totalWords / BATCH_SIZE);
            log.info("미완성 단어 총 {}개 발견. (총 {}개 배치 작업 시작)", totalWords, totalBatches);

            // 3. 배치 단위 AI 호출 및 파일 실시간 업데이트
            for (int i = 0; i < totalWords; i += BATCH_SIZE) {
                int currentBatch = (i / BATCH_SIZE) + 1;
                List<String> batchWords = targetWords.subList(i, Math.min(i + BATCH_SIZE, totalWords));

                log.info("[{}/{}] 배치 AI 요청 중... (단어 범위: {} ~ {})",
                        currentBatch, totalBatches, batchWords.getFirst(), batchWords.getLast());

                List<WordDefinitionResponseDto> batchResults = geminiAiUtil.generateWordDefinitions(batchWords);

                if (batchResults != null && !batchResults.isEmpty()) {
                    // 매 배치 성공 시 파일 덮어쓰기
                    excelUtil.updateBatchToExcel(tempFile, batchResults);
                    log.info("[{}/{}] 배치 완료 및 엑셀 반영 성공", currentBatch, totalBatches);
                } else {
                    log.warn("[{}/{}] 배치 실패 - 데이터 건너뛰고 계속 진행", currentBatch, totalBatches);
                }
            }

            log.info("모든 배치 작업 완료.");
            return Files.readAllBytes(tempFile.toPath());

        } finally {
            // 임시 파일 삭제
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
