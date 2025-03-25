package shop.nongdam.nongdambackend.ingredient.domain;

import jakarta.persistence.*;
import lombok.*;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.global.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseEntity {
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_ugly_reason_id", nullable = false)
    private IngredientUglyReason ingredientUglyReason;

    @Column(name = "ingredient_description", nullable = false)
    private String ingredientDescription;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientImage> ingredientImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_category_id", nullable = false)
    private IngredientCategory ingredientCategory;

    private Long price;

    @Setter
    @Column(name = "gpt_comment", length = 2200)
    private String gptComment;

    @Builder
    public Ingredient(Farm farm, String ingredientName, IngredientUglyReason ingredientUglyReason,
                      String ingredientDescription, IngredientCategory ingredientCategory, Long price) {
        this.farm = farm;
        this.ingredientName = ingredientName;
        this.ingredientUglyReason = ingredientUglyReason;
        this.ingredientDescription = ingredientDescription;
        this.ingredientCategory = ingredientCategory;
        this.price = price;
        this.gptComment = "";
    }

    public void addIngredientImage(IngredientImage newIngredientImage) {
        this.ingredientImages.add(newIngredientImage);
    }
}
