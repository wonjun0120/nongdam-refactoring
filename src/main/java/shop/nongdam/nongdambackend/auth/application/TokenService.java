package shop.nongdam.nongdambackend.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.auth.api.dto.request.RefreshTokenRequestDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberLoginResponseDTO;
import shop.nongdam.nongdambackend.auth.exception.InvalidTokenException;
import shop.nongdam.nongdambackend.global.jwt.TokenProvider;
import shop.nongdam.nongdambackend.global.jwt.api.dto.TokenDTO;
import shop.nongdam.nongdambackend.global.jwt.domain.Token;
import shop.nongdam.nongdambackend.global.jwt.domain.repository.TokenRepository;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TokenDTO getToken(MemberLoginResponseDTO memberLoginResponseDto) {
        TokenDTO tokenDTO = tokenProvider.generateToken(memberLoginResponseDto.findMember().getEmail(),
                memberLoginResponseDto.findMember().getRole());

        tokenSaveAndUpdate(memberLoginResponseDto, tokenDTO);

        return tokenDTO;
    }

    private void tokenSaveAndUpdate(MemberLoginResponseDTO memberLoginResponseDto, TokenDTO tokenDTO) {
        if (!tokenRepository.existsByMember(memberLoginResponseDto.findMember())) {
            tokenRepository.save(Token.builder()
                    .member(memberLoginResponseDto.findMember())
                    .refreshToken(tokenDTO.refreshToken())
                    .build());
        }

        refreshTokenUpdate(memberLoginResponseDto, tokenDTO);
    }

    private void refreshTokenUpdate(MemberLoginResponseDTO memberLoginResponseDto, TokenDTO tokenDTO) {
        Token token = tokenRepository.findByMember(memberLoginResponseDto.findMember()).orElseThrow();
        token.refreshTokenUpdate(tokenDTO.refreshToken());
    }

    @Transactional
    public TokenDTO generateAccessToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        if (isInvalidRefreshToken(refreshTokenRequestDTO.refreshToken())) {
            throw new InvalidTokenException();
        }

        Token token = tokenRepository.findByRefreshToken(refreshTokenRequestDTO.refreshToken()).orElseThrow();
        Member member = memberRepository.findById(token.getMember().getId()).orElseThrow();

        return tokenProvider.generateAccessTokenByRefreshToken(member.getEmail(), token.getRefreshToken());
    }

    private boolean isInvalidRefreshToken(String refreshToken) {
        return !tokenRepository.existsByRefreshToken(refreshToken) || !tokenProvider.validateToken(refreshToken);
    }
}
