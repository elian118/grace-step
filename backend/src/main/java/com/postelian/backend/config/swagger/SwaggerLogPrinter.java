package com.postelian.backend.config.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SwaggerLogPrinter implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String port;

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String baseUrl = "http://localhost:" + port + swaggerPath;

        log.info("==========================================================================================");
        log.info("GraceStep Backend 애플리케이션이 정상적으로 시작되었습니다!");
        log.info("아래 각 도메인별 Swagger UI 링크를 통해 API 명세서를 확인할 수 있습니다:");
        log.info("------------------------------------------------------------------------------------------");
        log.info("전체 API       : {}", baseUrl);
        log.info("사용자 관리    : {}?group=01.+USER-DOMAIN", baseUrl);
        log.info("학생/출석 관리 : {}?group=02.+STUDENT-DOMAIN", baseUrl);
        log.info("AI 학습 보조   : {}?group=03.+AI-DOMAIN", baseUrl);
        log.info("==========================================================================================");
    }
}
