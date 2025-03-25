package shop.nongdam.nongdambackend.member.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class MemberNotFoundException extends NotFoundGroupException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        this("Error: 존재하지 않는 회원입니다");
    }
}
