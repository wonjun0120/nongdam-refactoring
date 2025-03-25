package shop.nongdam.nongdambackend.ingredient.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.ingredient.domain.IngredientCategory;

import java.util.Optional;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    Optional<IngredientCategory> findByName(String name);
}
