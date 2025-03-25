package shop.nongdam.nongdambackend.restaurant.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

@Builder
public record RestaurantTagResponseDTO(
        Long restaurantId,
        String restaurantName,
        String restaurantImage,
        String address,
        String farmProduce,
        String menuName

) {
    public static RestaurantTagResponseDTO from(Restaurant restaurant) {
        return RestaurantTagResponseDTO.builder()
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .restaurantImage(restaurant.getRestaurantImage())
                .address(restaurant.getAddress())
                .farmProduce(restaurant.getMainMenu().getFarmProduce())
                .menuName(restaurant.getMainMenu().getName())
                .build();
    }
}
