package shop.nongdam.nongdambackend.ingredient.api.dto.response;

import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;

import java.util.List;

public record IngredientInfoResponseDTOs (
        List<IngredientInfoResponseDTO> ingredientInfoResponseDTOs,
        PageInfoResDto pageInfoResDto
){
    public static IngredientInfoResponseDTOs of(
            List<IngredientInfoResponseDTO> ingredientInfoResponseDTOs,
            PageInfoResDto pageInfoResDto
    ){
        return new IngredientInfoResponseDTOs(ingredientInfoResponseDTOs, pageInfoResDto);
    }
}

