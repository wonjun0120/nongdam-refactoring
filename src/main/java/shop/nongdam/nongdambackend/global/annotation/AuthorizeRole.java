package shop.nongdam.nongdambackend.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import shop.nongdam.nongdambackend.member.domain.Role;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeRole {
    Role[] value() default {};
}