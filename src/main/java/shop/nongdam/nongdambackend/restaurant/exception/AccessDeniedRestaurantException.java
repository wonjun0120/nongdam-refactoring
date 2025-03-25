package shop.nongdam.nongdambackend.restaurant.exception;

import shop.nongdam.nongdambackend.global.error.exception.AccessDeniedGroupException;

public class AccessDeniedRestaurantException extends AccessDeniedGroupException {
    public AccessDeniedRestaurantException(String message) {
        super(message);
    }

    public AccessDeniedRestaurantException() {
        this("Error: 해당 식당에 접근 권한이 없습니다");
    }

}
