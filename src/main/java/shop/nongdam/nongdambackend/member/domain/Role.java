package shop.nongdam.nongdambackend.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("일반 사용자"),
    ROLE_PRODUCER("농산물 생산자"),
    ROLE_RESTAURANT("요식업 종사자"),
    ;

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
