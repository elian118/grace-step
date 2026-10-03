package com.postelian.backend.domain.word.controller;

import com.postelian.backend.domain.word.service.WordExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Word Excel", description = "교육부 영단어 엑셀 자동 완성 API")
@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordExcelController {

    private final WordExcelService wordExcelService;

    @Operation(summary = "영단어 엑셀 자동 채우기", description = "미완성된 영단어 엑셀 파일을 업로드하면 Gemini가 품사, 뜻, 예문을 채워 완성된 엑셀을 반환합니다.")
    @PostMapping(value = "/fill-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> fillWordExcel(
            @Parameter(description = "업로드할 엑셀 파일 (.xlsx)", required = true)
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        byte[] processedExcel = wordExcelService.processAndFillExcel(file.getInputStream());

        String outputFileName = "completed_words.xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFileName + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(processedExcel);
    }
}
