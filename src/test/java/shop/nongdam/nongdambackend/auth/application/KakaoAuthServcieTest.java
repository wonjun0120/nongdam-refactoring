package shop.nongdam.nongdambackend.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoAuthServcieTest {

    @InjectMocks KakaoAuthService kakaoAuthServcie;

    @Test
    @DisplayName("getIdToken: 정상적인 요청이 들어왔을 때, IdToken을 반환한다.")
    void getIdToken_whenValidRequest_thenReturnIdToken() {
        // given
        String code = "valid_code";
        String dummyResponse = """
            {
              "token_type": "bearer",
              "access_token": "some-access-token",
              "id_token": "dummy-id-token"
            }
            """;

        ResponseEntity<String> mockResponse = new ResponseEntity<>(dummyResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(mockResponse);

        // when
        var result = kakaoAuthServcie.getIdToken(code);

        // then
        assertThat(result).isNotNull();
    }
}
