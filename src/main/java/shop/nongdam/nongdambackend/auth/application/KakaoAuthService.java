package shop.nongdam.nongdambackend.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import shop.nongdam.nongdambackend.auth.api.dto.response.IdTokenResponseDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberInfoResponseDTO;
import shop.nongdam.nongdambackend.auth.exception.InvalidTokenException;
import shop.nongdam.nongdambackend.member.domain.SocialType;

@Slf4j
@Service
@Transactional(readOnly = true)
public class KakaoAuthService implements AuthService {
    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String JWT_DELIMITER = "\\.";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${oauth.kakao.redirect-url}")
    private String redirectUri;

    public KakaoAuthService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public IdTokenResponseDTO getIdToken(String code) {
        ResponseEntity<String> response = requestKakaoToken(code);
        return parseIdTokenFromResponse(response);
    }

    private ResponseEntity<String> requestKakaoToken(String code) {
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = createKakaoTokenRequest(code);
        return restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST, kakaoTokenRequest, String.class);
    }

    private HttpEntity<MultiValueMap<String, String>> createKakaoTokenRequest(String code) {
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> params = createTokenRequestParams(code);
        return new HttpEntity<>(params, headers);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createTokenRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return params;
    }

    private IdTokenResponseDTO parseIdTokenFromResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return extractIdToken(response.getBody());
        }
        throw new InvalidTokenException("카카오 엑세스 토큰을 가져오는데 실패했습니다.");
    }

    private IdTokenResponseDTO extractIdToken(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode idToken = jsonNode.get("id_token");
            return new IdTokenResponseDTO(idToken);
        } catch (Exception e) {
            throw new InvalidTokenException("ID 토큰을 파싱하는데 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public MemberInfoResponseDTO getUserInfo(String idToken) {
        String decodePayload = decodeIdTokenPayload(idToken);
        return parseMemberInfo(decodePayload);
    }

    private String decodeIdTokenPayload(String idToken) {
        String payload = extractPayload(idToken);
        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }

    private String extractPayload(String idToken) {
        log.info("idToken: {}", idToken);
        return idToken.split(JWT_DELIMITER)[1];
    }

    private MemberInfoResponseDTO parseMemberInfo(String decodePayload) {
        try {
            return objectMapper.readValue(decodePayload, MemberInfoResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new InvalidTokenException("id 토큰을 읽을 수 없습니다.");
        }
    }

    @Override
    public String getProvider() {
        return String.valueOf(SocialType.KAKAO).toLowerCase();
    }
}
