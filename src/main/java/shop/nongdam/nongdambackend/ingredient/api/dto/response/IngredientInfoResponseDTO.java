package shop.nongdam.nongdambackend.ingredient.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmSummaryResponseDTO;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;
import shop.nongdam.nongdambackend.ingredient.domain.IngredientImage;

import java.util.List;

@Builder
public record IngredientInfoResponseDTO(
        Long ingredientId,
        String ingredientName,
        String ingredientCategory,
        Long price,
        FarmSummaryResponseDTO farmSummaryResponseDTO,
        String uglyReason,
        String ingredientDescription,
        List<String> ingredientImages
) {
    public static IngredientInfoResponseDTO from(Ingredient ingredient){
        return IngredientInfoResponseDTO.builder()
                .ingredientId(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .ingredientCategory(ingredient.getIngredientCategory().getName())
                .price(ingredient.getPrice())
                .farmSummaryResponseDTO(FarmSummaryResponseDTO.from(ingredient.getFarm()))
                .uglyReason(ingredient.getIngredientUglyReason().getName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .ingredientImages(ingredient.getIngredientImages()
                        .stream().map(IngredientImage::getImageUrl).toList())
                .build();
    }
}
