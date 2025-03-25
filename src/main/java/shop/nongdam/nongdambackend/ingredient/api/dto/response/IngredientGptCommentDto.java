package shop.nongdam.nongdambackend.ingredient.api.dto.response;

import lombok.Builder;

@Builder
public record IngredientGptCommentDto(
        String content
) {
    public static IngredientGptCommentDto from(String content) {
        return IngredientGptCommentDto.builder()
                .content(content)
                .build();
    }
}
