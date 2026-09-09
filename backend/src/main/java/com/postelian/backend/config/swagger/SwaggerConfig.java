package com.postelian.backend.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GraceStep API Document")
                        .description("초5~중2 영문법 수업 관리 프로젝트 API 명세서")
                        .version("v1.0.0"));
    }

    @Bean
    public GroupedOpenApi totalApi() {
        return GroupedOpenApi.builder()
                .group("00. TOTAL-API")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("01. USER-DOMAIN")
                .pathsToMatch("/api/v1/user/**")
                .packagesToScan("com.postelian.backend.domain.user")
                .build();
    }

    @Bean
    public GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("02. STUDENT-DOMAIN")
                .pathsToMatch("/api/v1/students/**")
                .packagesToScan("com.postelian.backend.domain.student")
                .build();
    }

    @Bean
    public GroupedOpenApi aiApi() {
        return GroupedOpenApi.builder()
                .group("03. AI-DOMAIN")
                .pathsToMatch("/api/v1/ai/**")
                .packagesToScan("com.postelian.backend.domain.ai")
                .build();
    }

    @Bean
    public GroupedOpenApi vocabularyApi() {
        return GroupedOpenApi.builder()
                .group("04. CLASSROOM-DOMAIN")
                .pathsToMatch("/api/v1/vocabularies/**")
                .packagesToScan("com.postelian.backend.domain.classroom")
                .build();
    }

    @Bean
    public GroupedOpenApi examApi() {
        return GroupedOpenApi.builder()
                .group("05. EXAM-DOMAIN")
                .pathsToMatch("/api/v1/exams/**")
                .packagesToScan("com.postelian.backend.domain.exam")
                .build();
    }
}
