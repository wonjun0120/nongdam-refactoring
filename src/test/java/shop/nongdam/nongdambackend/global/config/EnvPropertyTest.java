package shop.nongdam.nongdambackend.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DisplayName("환경 변수 로딩 확인 테스트")
@ActiveProfiles("test")
public class EnvPropertyTest {

    @Autowired
    private Environment env;

    @Test
    @DisplayName("환경 변수 출력")
    void printLoadedEnvVars() {
        System.out.println("SPRING_PROFILES_ACTIVE: " + env.getProperty("spring.profiles.active"));
        System.out.println("SPRING_DATASOURCE_URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("OPENAI_MODEL: " + env.getProperty("openai.model"));
    }
}
