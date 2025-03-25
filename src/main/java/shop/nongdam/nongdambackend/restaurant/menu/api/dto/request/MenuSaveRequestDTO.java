package shop.nongdam.nongdambackend.restaurant.menu.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuSaveRequestDTO(
        @NotNull(message = "식당 id는 필수 입력값입니다.")
        Long restaurantId,

        @NotBlank(message = "메뉴 이름은 필수 입력값입니다.")
        String name,

        @Min(value = 1, message = "메뉴 가격은 1 이상이어야 합니다.")
        int price,

        String farmProduce,

        String mainDescription,

        String subDescription,

        boolean isMainMenu
) {
}
