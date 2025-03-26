package shop.nongdam.nongdambackend.farm.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class FarmBadgeNotFoundException extends NotFoundGroupException {
    public FarmBadgeNotFoundException(String message) {
        super(message);
    }
    public FarmBadgeNotFoundException() {
        this("Error: 농가 뱃지를 찾을 수 없습니다.");
    }

}
