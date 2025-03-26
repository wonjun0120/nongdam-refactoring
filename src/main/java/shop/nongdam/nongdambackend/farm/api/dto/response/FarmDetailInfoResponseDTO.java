package shop.nongdam.nongdambackend.farm.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.farm.domain.FarmBadge;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientInfoResponseDTO;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;

import java.util.List;

@Builder
public record FarmDetailInfoResponseDTO(
        Long farmId,
        String farmName,
        String profileImage,
        String farmRepresentative,
        String phoneNumber,
        String businessRegistrationNumber,
        String address,
        String region,
        Double latitude,
        Double longitude,
        List<FarmBadgeResponseDTO> badges,
        List<IngredientInfoResponseDTO> ingredients
) {
    public static FarmDetailInfoResponseDTO from(Farm farm) {
        return FarmDetailInfoResponseDTO.builder()
                .farmId(farm.getId())
                .farmName(farm.getFarmName())
                .profileImage(farm.getProfileImage())
                .farmRepresentative(farm.getFarmRepresentative())
                .phoneNumber(farm.getPhoneNumber())
                .businessRegistrationNumber(String.valueOf(farm.getBusinessRegistrationNumber()))
                .address(farm.getAddress())
                .region(farm.getRegion().getName())
                .latitude(farm.getLatitude())
                .longitude(farm.getLongitude())
                .badges(farm.getFarmBadges().stream()
                        .map(FarmBadgeResponseDTO::from).toList())
                .ingredients(farm.getIngredients().stream()
                        .map(IngredientInfoResponseDTO::from).toList())
                .build();
    }
}
