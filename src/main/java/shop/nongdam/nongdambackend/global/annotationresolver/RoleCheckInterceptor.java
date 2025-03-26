package shop.nongdam.nongdambackend.global.annotationresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.nongdam.nongdambackend.auth.api.dto.request.TokenRequestDTO;
import shop.nongdam.nongdambackend.global.annotation.AuthorizeRole;
import shop.nongdam.nongdambackend.global.jwt.TokenProvider;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.exception.RoleAccessDeniedException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleCheckInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        AuthorizeRole roleAnnotation = handlerMethod.getMethodAnnotation(AuthorizeRole.class);

        if (roleAnnotation == null) {
            return true;
        }

        String token = resolveToken(request);
        if (token == null) {
            throw new RoleAccessDeniedException("Error: 토큰이 존재하지 않습니다.");
        }

        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO(token);
        Role userRole = tokenProvider.getMemberRoleFromToken(tokenRequestDTO);

        if (userRole == null) {
            throw new RoleAccessDeniedException("Error: 토큰에서 역할 정보를 찾을 수 없습니다.");
        }

        Role[] allowedRoles = roleAnnotation.value();
        if (allowedRoles.length > 0 && !Arrays.asList(allowedRoles).contains(userRole)) {
            String requiredRoles = Arrays.toString(allowedRoles);
            throw new RoleAccessDeniedException(
                    String.format("Error: 접근 거부되었습니다. 필요한 권한: %s", requiredRoles)
            );
        }

        return true;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
