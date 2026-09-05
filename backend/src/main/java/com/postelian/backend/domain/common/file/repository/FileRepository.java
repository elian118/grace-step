package com.postelian.backend.domain.common.file.repository;

import com.postelian.backend.domain.common.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByFileFolderKey(String fileFolderKey);

    @Query("SELECT f FROM File f WHERE f.type = :type AND f.createdAt BETWEEN :startDate AND :endDate")
    List<File> findByTypeAndCreatedAtBetween(@Param("type") String type, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
}
