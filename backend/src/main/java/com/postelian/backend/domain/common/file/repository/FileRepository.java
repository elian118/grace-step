package com.postelian.backend.domain.common.file.repository;

import com.postelian.backend.domain.common.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByFileFolderKey(String fileFolderKey);
}
