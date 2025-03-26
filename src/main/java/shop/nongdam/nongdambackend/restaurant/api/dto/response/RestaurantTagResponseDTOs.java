package shop.nongdam.nongdambackend.restaurant.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;

@Builder
public record RestaurantTagResponseDTOs(
        List<RestaurantTagResponseDTO> restaurantTagResponseDTOs,
        PageInfoResDto pageInfoResDto

) {
    public static RestaurantTagResponseDTOs of(List<RestaurantTagResponseDTO> restaurantTagResponseDTOs,
                                               PageInfoResDto pageInfoResDto) {
        return RestaurantTagResponseDTOs.builder()
                .restaurantTagResponseDTOs(restaurantTagResponseDTOs)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
