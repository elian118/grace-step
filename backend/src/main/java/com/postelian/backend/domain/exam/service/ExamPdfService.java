package com.postelian.backend.domain.exam.service;

import com.postelian.backend.domain.common.file.dto.FileUploadRequestDto;
import com.postelian.backend.domain.common.file.service.FileService;
import com.postelian.backend.domain.exam.entity.ExamQuestion;
import com.postelian.backend.domain.exam.repository.ExamQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamPdfService {

    private final FileService fileService;
    private final ExamQuestionRepository examQuestionRepository;

    @Transactional
    public void uploadExamPdf(String title, MultipartFile pdfFile, String userId) {
        String fileFolderKey = UUID.randomUUID().toString();
        String type = "EXAM";

        // 1. 파일 저장 (FileService 사용)
        FileUploadRequestDto.Single uploadRequest = new FileUploadRequestDto.Single();
        uploadRequest.setFile(pdfFile);
        uploadRequest.setFileFolderKey(fileFolderKey);
        uploadRequest.setType(type);
        
        var savedFileDto = fileService.uploadFile(uploadRequest, userId);

        // 2. 해당 타이틀을 가진 시험지 데이터에 fileId 매핑
        List<ExamQuestion> questions = examQuestionRepository.findByTitle(title);
        var savedFileEntity = fileService.getFileEntity(savedFileDto.getId());
        
        for (ExamQuestion question : questions) {
            question.updateQuestion(question.getTitle(), question.getUnit(), question.getQuestion(),
                                    question.getAnswer(), question.getExplanation(), savedFileEntity, userId);
        }
    }
}
