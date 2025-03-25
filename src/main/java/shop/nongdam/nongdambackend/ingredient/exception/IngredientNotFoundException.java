package shop.nongdam.nongdambackend.ingredient.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class IngredientNotFoundException extends NotFoundGroupException {
    public IngredientNotFoundException(String message) {
        super(message);
    }
    public IngredientNotFoundException() {
        super("해당 식료품을 찾을 수 없습니다.");
    }
}
