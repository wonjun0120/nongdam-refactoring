package shop.nongdam.nongdambackend.ingredient.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.ingredient.domain.IngredientUglyReason;

import java.util.Optional;

public interface IngredientUglyReasonRepository extends JpaRepository<IngredientUglyReason, Long> {
    Optional<IngredientUglyReason> findByName(String name);
}


