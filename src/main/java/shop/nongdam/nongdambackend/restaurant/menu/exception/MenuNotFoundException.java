package shop.nongdam.nongdambackend.restaurant.menu.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class MenuNotFoundException extends NotFoundGroupException {
        public MenuNotFoundException() {
            super("Error: 메뉴가 존재하지 않습니다.");
        }

        public MenuNotFoundException(String message) {
            super(message);
        }
}
