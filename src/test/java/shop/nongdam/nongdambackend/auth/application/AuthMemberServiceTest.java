package shop.nongdam.nongdambackend.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberInfoResponseDTO;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthMemberServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PICTURE = "test_picture";
    private static final String TEST_NICKNAME = "test_nickname";

    @InjectMocks private AuthMemberService authMemberService;

    @Mock private MemberRepository memberRepository;


    @Test
    @DisplayName("saveMemberInfo: 정상적인 요청이 들어왔을 때, 회원 정보를 저장한다.")
    void saveMemberInfo_whenValidRequest_thenSaveMemberInfo() {
        // given
        MemberInfoResponseDTO memberInfoResponseDTO = createMemberInfo();
        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        var result = authMemberService.saveMemberInfo(memberInfoResponseDTO, SocialType.KAKAO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.findMember().getEmail()).isEqualTo(TEST_EMAIL);
        verify(memberRepository).save(any());
    }


//    @Test
//    @DisplayName("saveMemberInfo: 이미 존재하는 회원의 이메일로 요청이 들어왔을 때, 예외를 발생시킨다.")
//    void saveMemberInfo_whenEmailAlreadyExists_thenThrowException() {
//        // given
//        MemberInfoResponseDTO memberInfoResponseDTO = createMemberInfo();
//        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(any()));
//
//        // when, then
//        assertThatThrownBy(() -> authMemberService.saveMemberInfo(memberInfoResponseDTO, SocialType.KAKAO))
//            .isInstanceOf(ExistsMemberEmailException.class);
//    }

    private MemberInfoResponseDTO createMemberInfo() {
        return new MemberInfoResponseDTO(
            TEST_EMAIL,
            TEST_PICTURE,
            TEST_NICKNAME
        );
    }
}
