package shop.nongdam.nongdambackend.ingredient.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class IngredientUglyReasonNotFoundException extends NotFoundGroupException {
    public IngredientUglyReasonNotFoundException(String message){
        super(message);
    }

    public IngredientUglyReasonNotFoundException(){
        this("Error: 못난이 이유를 찾을 수 없습니다");
    }
}
