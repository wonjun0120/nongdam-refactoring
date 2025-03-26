package shop.nongdam.nongdambackend.auth.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberInfoResponseDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberLoginResponseDTO;
import shop.nongdam.nongdambackend.auth.exception.EmailNotFoundException;
import shop.nongdam.nongdambackend.auth.exception.ExistsMemberEmailException;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthMemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberLoginResponseDTO saveMemberInfo(MemberInfoResponseDTO memberInfoResponseDTO, SocialType provider) {
        validateEmail(memberInfoResponseDTO.email());

        Member member = findOrCreateMember(memberInfoResponseDTO, provider);

        validateSocialType(provider, member);

        return MemberLoginResponseDTO.from(member);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new EmailNotFoundException();
        }
    }

    private Member findOrCreateMember(MemberInfoResponseDTO memberInfoResponseDTO, SocialType provider) {
        return memberRepository.findByEmail(memberInfoResponseDTO.email())
                .orElseGet(() -> createMember(memberInfoResponseDTO, provider));
    }

    private void validateSocialType(SocialType provider, Member member) {
        if (!provider.equals(member.getSocialType())) {
            throw new ExistsMemberEmailException();
        }
    }

    private Member createMember(MemberInfoResponseDTO memberInfoResponseDTO, SocialType provider) {
        String memberPicture = resolveMemberPicture(memberInfoResponseDTO.picture());

        return memberRepository.save(buildNewMember(memberInfoResponseDTO, provider, memberPicture));
    }

    private String resolveMemberPicture(String picture) {
        return Optional.ofNullable(picture)
                .map(this::convertToHighRes)
                .orElseThrow();
    }

    private String convertToHighRes(String url) {
        return url.replace("s96-c", "s2048-c");
    }

    private Member buildNewMember(MemberInfoResponseDTO memberInfoResponseDTO, SocialType provider, String memberPicture) {
        return Member.builder()
                .email(memberInfoResponseDTO.email())
                .name(memberInfoResponseDTO.nickname())
                .picture(memberPicture)
                .socialType(provider)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .certificate("")
                .build();
    }
}
