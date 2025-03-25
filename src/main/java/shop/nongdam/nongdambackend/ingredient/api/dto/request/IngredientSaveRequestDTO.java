package shop.nongdam.nongdambackend.ingredient.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IngredientSaveRequestDTO(
        @NotBlank(message = "식료품 이름은 필수 입력값입니다.")
        String ingredientName,
        @NotBlank(message = "식료품 카테고리는 필수 입력값입니다.")
        String ingredientCategory,
        @NotBlank(message = "못난이인 이유는 필수 입력값입니다.")
        String uglyReason,
        @NotBlank(message = "식료품 상세 설명은 필수 입력값입니다.")
        String ingredientDescription,
        @NotBlank(message = "식료품 kg당 가격은 필수 입력값입니다.")
        Long price
) {
}
