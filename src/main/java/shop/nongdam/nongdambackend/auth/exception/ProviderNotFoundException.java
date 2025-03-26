package shop.nongdam.nongdambackend.auth.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class ProviderNotFoundException extends NotFoundGroupException {
    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException() {
        this("Error: 존재하지 않는 제공자 입니다.");
    }
}
