package shop.nongdam.nongdambackend.ingredient.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;

public interface IngredientCustomRepository {
    Page<Ingredient> findAllIngredients(Pageable pageable);
}
