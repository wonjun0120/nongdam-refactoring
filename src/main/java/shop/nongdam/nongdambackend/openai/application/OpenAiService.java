package shop.nongdam.nongdambackend.openai.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.nongdam.nongdambackend.openai.api.dto.request.ChatGPTRequest;
import shop.nongdam.nongdambackend.openai.api.dto.response.ChatGPTResponse;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${spring.openai.model}")
    private String model;

    @Value("${spring.openai.api-url}")
    private String apiURL;

    @Value("${spring.openai.secret}")
    private String secretKey;

    private final RestTemplate template;

    public String chat(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ChatGPTResponse> response = template
                .exchange(apiURL, HttpMethod.POST, entity, ChatGPTResponse.class);

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}