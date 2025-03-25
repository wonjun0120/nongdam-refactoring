package shop.nongdam.nongdambackend.auth.api.dto.response;

public record MemberInfoResponseDTO(
        String email,
        String picture,
        String nickname
) {
}
