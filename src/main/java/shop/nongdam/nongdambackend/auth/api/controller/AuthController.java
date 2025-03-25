package shop.nongdam.nongdambackend.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.nongdam.nongdambackend.auth.api.dto.request.RefreshTokenRequestDTO;
import shop.nongdam.nongdambackend.auth.api.dto.request.TokenRequestDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.IdTokenResponseDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberInfoResponseDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberLoginResponseDTO;
import shop.nongdam.nongdambackend.auth.application.AuthMemberService;
import shop.nongdam.nongdambackend.auth.application.AuthService;
import shop.nongdam.nongdambackend.auth.application.AuthServiceFactory;
import shop.nongdam.nongdambackend.auth.application.TokenService;
import shop.nongdam.nongdambackend.global.jwt.api.dto.TokenDTO;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.member.domain.SocialType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController implements AuthDocs {
    private final AuthServiceFactory authServiceFactory;
    private final AuthMemberService authMemberService;
    private final TokenService tokenService;

    @Override
    @GetMapping("oauth2/callback/{provider}")
    public IdTokenResponseDTO callback(@PathVariable String provider, @RequestParam String code) {
        return authServiceFactory.getAuthService(provider).getIdToken(code);
    }

    @Override
    @PostMapping("/{provider}/token")
    public ApiResponseTemplate<TokenDTO> generateAccessAndRefreshToken(@PathVariable String provider,
                                                                       @RequestBody TokenRequestDTO tokenRequestDTO) {
        AuthService authService = authServiceFactory.getAuthService(provider);
        MemberInfoResponseDTO memberInfoResponseDTO = authService.getUserInfo(tokenRequestDTO.authCode());

        MemberLoginResponseDTO memberLoginResponseDto = authMemberService.saveMemberInfo(memberInfoResponseDTO,
                SocialType.valueOf(provider.toUpperCase()));
        TokenDTO tokenDTO = tokenService.getToken(memberLoginResponseDto);

        return ApiResponseTemplate.ok("토큰 발급", tokenDTO);
    }

    @Override
    @PostMapping("/token/access")
    public ApiResponseTemplate<TokenDTO> generateAccessToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        TokenDTO getToken = tokenService.generateAccessToken(refreshTokenRequestDTO);

        return ApiResponseTemplate.ok("액세스 토큰 발급", getToken);
    }
}
