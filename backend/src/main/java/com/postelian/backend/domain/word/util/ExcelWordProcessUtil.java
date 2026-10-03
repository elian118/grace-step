package com.postelian.backend.domain.word.util;

import com.postelian.backend.domain.word.dto.WordDefinitionResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class ExcelWordProcessUtil {

    private final DataFormatter dataFormatter = new DataFormatter();

    /**
     * 1. 엑셀에서 미완성 단어 추출
     * - F열(품사) 또는 G열(뜻)이 비어있는 경우 (신규 채우기 대상)
     * - 이미 F열(품사)에 '동사'가 들어가 있지만 D열(과거) 또는 E열(과거분사)이 비어있는 경우 (과거형 채우기 대상)
     */
    public List<String> extractTargetWords(File file) throws Exception {
        Set<String> targetWordsSet = new LinkedHashSet<>(); // 중복 제거 및 순서 보장

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // C열(2): 표제어 읽기
                Cell wordCell = row.getCell(2);
                String word = getCellValueAsString(wordCell);

                if (word.isEmpty()) {
                    wordCell = row.getCell(1); // B열 예비 검사
                    word = getCellValueAsString(wordCell);
                }

                if (word.isEmpty()) continue;

                Cell pastCell = row.getCell(3);      // D열(3): 과거
                Cell pastPartCell = row.getCell(4);  // E열(4): 과거분사
                Cell posCell = row.getCell(5);       // F열(5): 품사
                Cell meaningCell = row.getCell(6);   // G열(6): 뜻

                boolean isPosEmpty = isCellEmpty(posCell);
                boolean isMeaningEmpty = isCellEmpty(meaningCell);
                String posValue = getCellValueAsString(posCell);

                // 조건 1: 품사나 뜻이 비어있는 경우
                if (isPosEmpty || isMeaningEmpty) {
                    targetWordsSet.add(word);
                }
                // 조건 2: 품사가 '동사'(타동사, 자동사 등)인데 과거형이나 과거분사형이 비어있는 경우
                else if (posValue.contains("동사")) {
                    if (isCellEmpty(pastCell) || isCellEmpty(pastPartCell)) {
                        targetWordsSet.add(word);
                    }
                }
            }
        }
        return new ArrayList<>(targetWordsSet);
    }

    /**
     * 2. 배치 결과를 엑셀 파일에 업데이트 (과거형/과거분사형 동사 첫 행 입력 지원)
     */
    public void updateBatchToExcel(File file, List<WordDefinitionResponseDto> batchResults) throws Exception {
        if (batchResults == null || batchResults.isEmpty()) return;

        File tempOutputFile = File.createTempFile("word_batch_out_", ".xlsx");

        try {
            List<RowData> updatedRows = new ArrayList<>();

            // 1. 기존 워크북 데이터 로드
            try (Workbook workbook = WorkbookFactory.create(file)) {
                Sheet sheet = workbook.getSheetAt(0);

                List<RowData> allRows = new ArrayList<>();
                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    RowData rowData = new RowData();
                    rowData.no = formatNoValue(getCellValueAsString(row.getCell(0))); // A열: No
                    rowData.original = getCellValueAsString(row.getCell(1));          // B열: 원본 목록
                    rowData.word = getCellValueAsString(row.getCell(2));              // C열: 표제어
                    rowData.past = getCellValueAsString(row.getCell(3));              // D열: 과거
                    rowData.pastParticiple = getCellValueAsString(row.getCell(4));    // E열: 과거분사
                    rowData.pos = getCellValueAsString(row.getCell(5));               // F열: 품사
                    rowData.meaning = getCellValueAsString(row.getCell(6));           // G열: 뜻
                    rowData.example = getCellValueAsString(row.getCell(7));           // H열: 예문
                    rowData.category = getCellValueAsString(row.getCell(8));          // I열: 구분 (초등/중등 등)
                    rowData.note = getCellValueAsString(row.getCell(9));              // J열: 참고사항

                    allRows.add(rowData);
                }

                // 2. 데이터 갱신 및 행 분리 처리
                Set<String> processedVerbWords = new HashSet<>(); // 과거형을 이미 채운 단어 추적용 Set

                for (RowData rd : allRows) {
                    WordDefinitionResponseDto matchedDto = findMatchedDto(batchResults, rd.word);

                    if (matchedDto != null && matchedDto.getDefinitions() != null && !matchedDto.getDefinitions().isEmpty()) {
                        List<WordDefinitionResponseDto.DefinitionItem> defs = matchedDto.getDefinitions();

                        // [사례 A] 신규 데이터 채우기 (기존 품사/뜻이 없던 경우)
                        if (rd.pos == null || rd.pos.isEmpty()) {
                            WordDefinitionResponseDto.DefinitionItem firstDef = defs.getFirst();
                            rd.pos = firstDef.getPartOfSpeech();
                            rd.meaning = firstDef.getMeaning();
                            rd.example = firstDef.getExample();

                            fillVerbConjugationIfNeeded(rd, firstDef, processedVerbWords);
                            updatedRows.add(rd);

                            for (int d = 1; d < defs.size(); d++) {
                                WordDefinitionResponseDto.DefinitionItem nextDef = defs.get(d);
                                RowData newRd = new RowData();
                                newRd.no = rd.no;
                                newRd.original = rd.original;
                                newRd.word = rd.word;
                                newRd.pos = nextDef.getPartOfSpeech();
                                newRd.meaning = nextDef.getMeaning();
                                newRd.example = nextDef.getExample();
                                newRd.category = rd.category;
                                newRd.note = rd.note;

                                fillVerbConjugationIfNeeded(newRd, nextDef, processedVerbWords);
                                updatedRows.add(newRd);
                            }
                        }
                        // [사례 B] 기존 품사/뜻은 이미 존재하고 과거/과거분사만 없어서 업데이트하는 경우
                        else {
                            if (rd.pos.contains("동사")) {
                                // AI 결과에서 동사 항목의 past/pastParticiple 찾아 입력
                                for (WordDefinitionResponseDto.DefinitionItem def : defs) {
                                    if (def.getPartOfSpeech() != null && def.getPartOfSpeech().contains("동사")) {
                                        fillVerbConjugationIfNeeded(rd, def, processedVerbWords);
                                        break;
                                    }
                                }
                            }
                            updatedRows.add(rd);
                        }
                    } else {
                        updatedRows.add(rd);
                    }
                }
            }

            // 3. 새로운 Workbook 작성 및 스타일 지정
            try (Workbook newWorkbook = WorkbookFactory.create(true)) {
                Sheet newSheet = newWorkbook.createSheet("Words");

                // --- 스타일 정의 ---
                // 1) 헤더 스타일 (연푸른색 배경 + 테두리 + 중앙정렬 + 볼드)
                CellStyle headerStyle = newWorkbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                setCellBorders(headerStyle);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                Font headerFont = newWorkbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // 2) 일반 데이터 셀 스타일 (테두리 + 수직 중앙정렬)
                CellStyle dataStyle = newWorkbook.createCellStyle();
                setCellBorders(dataStyle);
                dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // 3) 중앙정렬 데이터 스타일 (No, 구분 열 등)
                CellStyle centerDataStyle = newWorkbook.createCellStyle();
                setCellBorders(centerDataStyle);
                centerDataStyle.setAlignment(HorizontalAlignment.CENTER);
                centerDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // --- 데이터 작성 (A~J열 : 0~9번 셀) ---
                for (int i = 0; i < updatedRows.size(); i++) {
                    Row row = newSheet.createRow(i);
                    RowData rd = updatedRows.get(i);
                    boolean isHeader = (i == 0);

                    CellStyle currentStyle = isHeader ? headerStyle : dataStyle;

                    createStyledCell(row, 0, rd.no, isHeader ? headerStyle : centerDataStyle);
                    createStyledCell(row, 1, rd.original, currentStyle);
                    createStyledCell(row, 2, rd.word, currentStyle);
                    createStyledCell(row, 3, rd.past, currentStyle);
                    createStyledCell(row, 4, rd.pastParticiple, currentStyle);
                    createStyledCell(row, 5, rd.pos, currentStyle);
                    createStyledCell(row, 6, rd.meaning, currentStyle);
                    createStyledCell(row, 7, rd.example, currentStyle);
                    createStyledCell(row, 8, rd.category, isHeader ? headerStyle : centerDataStyle);
                    createStyledCell(row, 9, rd.note, currentStyle);
                }

                // 4) 셀 너비 자동 맞춰주기 (A~J열 : 0~9번 셀)
                for (int col = 0; col <= 9; col++) {
                    newSheet.autoSizeColumn(col);
                    int currentWidth = newSheet.getColumnWidth(col);
                    newSheet.setColumnWidth(col, Math.min(currentWidth + 1024, 20000));
                }

                try (FileOutputStream fos = new FileOutputStream(tempOutputFile)) {
                    newWorkbook.write(fos);
                }
            }

            // 4. 안전하게 원본 파일로 이동/덮어쓰기
            Files.move(tempOutputFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } finally {
            if (tempOutputFile.exists()) {
                tempOutputFile.delete();
            }
        }
    }

    /**
     * 동사 품사이면서 단어가 최초로 등장한 경우에만 과거/과거분사 채움
     */
    private void fillVerbConjugationIfNeeded(RowData rd, WordDefinitionResponseDto.DefinitionItem def, Set<String> processedVerbWords) {
        if (def.getPartOfSpeech() != null && def.getPartOfSpeech().contains("동사")) {
            String lowerWord = rd.word.toLowerCase();
            // 해당 단어의 동사 변형이 아직 채워지지 않은 최초 행인 경우
            if (!processedVerbWords.contains(lowerWord)) {
                if (def.getPast() != null && !def.getPast().isEmpty()) {
                    rd.past = def.getPast();
                    rd.pastParticiple = def.getPastParticiple();
                    processedVerbWords.add(lowerWord); // 채웠음을 기록
                }
            }
        }
    }

    private void setCellBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private void createStyledCell(Row row, int cellIndex, String value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private String formatNoValue(String rawNo) {
        if (rawNo == null || rawNo.isEmpty()) return "";
        if (rawNo.endsWith(".0")) {
            return rawNo.substring(0, rawNo.length() - 2);
        }
        return rawNo;
    }

    private WordDefinitionResponseDto findMatchedDto(List<WordDefinitionResponseDto> batchResults, String word) {
        if (word == null || word.isEmpty()) return null;
        for (WordDefinitionResponseDto dto : batchResults) {
            if (word.equalsIgnoreCase(dto.getWord())) {
                return dto;
            }
        }
        return null;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return dataFormatter.formatCellValue(cell).trim();
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null) return true;
        return getCellValueAsString(cell).isEmpty();
    }

    private static class RowData {
        String no;
        String original;
        String word;
        String past;
        String pastParticiple;
        String pos;
        String meaning;
        String example;
        String category;
        String note;
    }
}
