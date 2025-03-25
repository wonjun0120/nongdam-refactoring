package shop.nongdam.nongdambackend.ingredient.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientUglyReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ingredientUglyReason", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ingredient> ingredients;

}
