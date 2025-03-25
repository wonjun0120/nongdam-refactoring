package shop.nongdam.nongdambackend.restaurant.api.dto.response;

import java.util.List;
import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;

public record RestaurantInfoResponseDTOs(
        List<RestaurantInfoResponseDTO> restaurantInfoResponseDTOs,
        PageInfoResDto pageInfoResDto

) {
    public static RestaurantInfoResponseDTOs of(List<RestaurantInfoResponseDTO> restaurantInfoResponseDTOs,
                                                PageInfoResDto pageInfoResDto) {
        return new RestaurantInfoResponseDTOs(restaurantInfoResponseDTOs, pageInfoResDto);
    }
}
