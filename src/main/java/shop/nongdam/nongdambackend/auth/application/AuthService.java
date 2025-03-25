package shop.nongdam.nongdambackend.auth.application;

import shop.nongdam.nongdambackend.auth.api.dto.response.IdTokenResponseDTO;
import shop.nongdam.nongdambackend.auth.api.dto.response.MemberInfoResponseDTO;

public interface AuthService {
    MemberInfoResponseDTO getUserInfo(String authCode);

    String getProvider();

    IdTokenResponseDTO getIdToken(String code);
}
