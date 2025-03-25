package shop.nongdam.nongdambackend.auth.exception;


import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class EmailNotFoundException extends NotFoundGroupException {
    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException() {
        this("Error: 존재하지 않는 이메일 입니다.");
    }
}
