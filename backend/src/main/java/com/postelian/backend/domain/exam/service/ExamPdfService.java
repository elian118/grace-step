package com.postelian.backend.domain.exam.service;

import com.postelian.backend.domain.common.file.entity.File;
import com.postelian.backend.domain.common.file.repository.FileRepository;
import com.postelian.backend.domain.exam.entity.ExamQuestion;
import com.postelian.backend.domain.exam.repository.ExamQuestionRepository;
import com.postelian.backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamPdfService {

    private final FileUtil fileUtil;
    private final FileRepository fileRepository;
    private final ExamQuestionRepository examQuestionRepository;

    @Transactional
    public void uploadExamPdf(String title, MultipartFile pdfFile, String userId) throws IOException {
        String fileFolderKey = UUID.randomUUID().toString();
        String type = "EXAM_PDF";

        // 1. 파일 저장
        String filePath = fileUtil.saveFile(type, fileFolderKey, pdfFile);

        // 2. 파일 메타데이터 저장
        File fileEntity = File.builder()
                .fileFolderKey(fileFolderKey)
                .type(type)
                .originalFilename(pdfFile.getOriginalFilename())
                .storedFilename(pdfFile.getOriginalFilename()) // 간단히 원본명 사용
                .filePath(filePath)
                .fileSize(pdfFile.getSize())
                .createdBy(userId)
                .build();
        File savedFile = fileRepository.save(fileEntity);

        // 3. 해당 타이틀을 가진 시험지 데이터에 fileId 매핑
        List<ExamQuestion> questions = examQuestionRepository.findByTitle(title);
        for (ExamQuestion question : questions) {
            question.updateQuestion(question.getTitle(), question.getUnit(), question.getQuestion(),
                                    question.getAnswer(), question.getExplanation(), savedFile, userId);
        }
    }
}
