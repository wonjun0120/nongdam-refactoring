package shop.nongdam.nongdambackend.auth.api.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

public record IdTokenResponseDTO(
        JsonNode idToken
) {
}
