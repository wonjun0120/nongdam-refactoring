package shop.nongdam.nongdambackend.ingredient.exception;

import shop.nongdam.nongdambackend.global.error.exception.NotFoundGroupException;

public class IngredientCategoryNotFoundException extends NotFoundGroupException {
    public IngredientCategoryNotFoundException(String message){
        super(message);
    }

    public IngredientCategoryNotFoundException(){
        this("Error: 상품 카테고리를 찾을 수 없습니다");
    }
}
