package shop.nongdam.nongdambackend.auth.api.dto.response;

import lombok.Builder;

@Builder
public record AccessAndRefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}
