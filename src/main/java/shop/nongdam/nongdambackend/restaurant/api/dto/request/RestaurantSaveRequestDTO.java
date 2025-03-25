package shop.nongdam.nongdambackend.restaurant.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RestaurantSaveRequestDTO(
        @NotBlank(message = "식당 이름은 필수 입력값입니다.")
        String restaurantName,
        @NotBlank(message = "식당 대표자는 필수 입력값입니다.")
        String restaurantRepresentative,
        @NotBlank(message = "식당 전화번호는 필수 입력값입니다.")
        String phoneNumber,
        @NotBlank(message = "사업자 등록번호는 필수 입력값입니다.")
        String businessRegistrationNumber,
        @NotBlank(message = "주소는 필수 입력값입니다.")
        String address
) {
}
