package com.postelian.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    private String baseDir;
    private SubDir subDir = new SubDir();

    @Getter
    @Setter
    public static class SubDir {
        private String exams;
        private String attendances;
    }
}
