package shop.nongdam.nongdambackend.auth.api.dto.response;


import lombok.Builder;
import shop.nongdam.nongdambackend.member.domain.Member;

@Builder
public record MemberLoginResponseDTO(
        Member findMember
) {
    public static MemberLoginResponseDTO from(Member member) {
        return MemberLoginResponseDTO.builder()
                .findMember(member)
                .build();
    }
}
