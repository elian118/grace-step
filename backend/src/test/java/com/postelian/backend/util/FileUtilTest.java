package com.postelian.backend.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilTest {

    private FileUtil fileUtil;
    private final String tempUploadDir = "temp_uploads";

    @BeforeEach
    void setUp() {
        fileUtil = new FileUtil();
        ReflectionTestUtils.setField(fileUtil, "fileUploadDir", tempUploadDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        Path path = Paths.get(tempUploadDir);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Test
    void saveAndDeleteTest() throws IOException {
        // given
        String type = "testType";
        String fileFolderKey = "testKey";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        // when
        String filePath = fileUtil.saveFile(type, fileFolderKey, file);

        // then
        assertThat(Files.exists(Paths.get(filePath))).isTrue();

        // when (delete)
        fileUtil.deleteFile(type, fileFolderKey, "test.txt");

        // then
        assertThat(Files.exists(Paths.get(filePath))).isFalse();
    }
}
