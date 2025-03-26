package shop.nongdam.nongdambackend.restaurant.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class RestaurantNotFoundException extends NotFoundGroupException {

    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException() {
        this("Error: 식당을 찾을 수 없습니다.");
    }
}
