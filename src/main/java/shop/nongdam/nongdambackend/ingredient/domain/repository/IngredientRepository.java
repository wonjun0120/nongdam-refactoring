package shop.nongdam.nongdambackend.ingredient.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, IngredientCustomRepository {
}
