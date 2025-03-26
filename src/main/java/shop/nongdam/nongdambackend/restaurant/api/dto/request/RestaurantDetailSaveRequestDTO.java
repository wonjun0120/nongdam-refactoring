package shop.nongdam.nongdambackend.restaurant.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantDetailSaveRequestDTO(
        @NotNull(message = "식당 id는 필수 입력값입니다.")
        Long restaurantId,

        @NotNull(message = "위도는 필수 입력값입니다.")
        Double latitude,

        @NotNull(message = "경도는 필수 입력값입니다.")
        Double longitude,

        @NotBlank(message = "오픈 시간은 필수 입력값입니다.")
        String openTime,

        @NotBlank(message = "마감 시간은 필수 입력값입니다.")
        String closeTime,

        @NotBlank(message = "주의 사항은 필수 입력값입니다.")
        String precautions
) {
}
