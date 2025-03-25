package shop.nongdam.nongdambackend.global.annotationresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.nongdam.nongdambackend.auth.api.dto.request.TokenRequestDTO;
import shop.nongdam.nongdambackend.global.annotation.CurrentMemberEmail;
import shop.nongdam.nongdambackend.global.jwt.TokenProvider;

@Component
public class CurrentMemberEmailArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;

    public CurrentMemberEmailArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentMemberEmail.class) != null;
    }

    @Override
    @NonNull
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenRequestDTO tokenReqDto = new TokenRequestDTO(token);
            String email = tokenProvider.getMemberEmailFromToken(tokenReqDto);
            if (email != null) {
                return email;
            }
        }

        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }
}