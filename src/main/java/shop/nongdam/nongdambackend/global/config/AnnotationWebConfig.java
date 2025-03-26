package shop.nongdam.nongdambackend.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.nongdam.nongdambackend.global.annotationresolver.CurrentMemberEmailArgumentResolver;
import shop.nongdam.nongdambackend.global.annotationresolver.RoleCheckInterceptor;

@Configuration
@RequiredArgsConstructor
public class AnnotationWebConfig implements WebMvcConfigurer {

    private final CurrentMemberEmailArgumentResolver currentMemberEmailArgumentResolver;
    private final RoleCheckInterceptor roleCheckInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberEmailArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**");
    }
}