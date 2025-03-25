package shop.nongdam.nongdambackend.global.jwt.api.dto;

import lombok.Builder;

@Builder
public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
