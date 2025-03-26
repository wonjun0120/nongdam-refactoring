package shop.nongdam.nongdambackend.farm.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.farm.domain.Farm;

@Builder
public record FarmInfoResponseDTO(
        Long farmId,
        String farmName,
//        String profileImage,
        String farmRepresentative,
        String phoneNumber,
        String businessRegistrationNumber,
        String address,
        String region
//        Double latitude,
//        Double longitude

){
    public static FarmInfoResponseDTO from(Farm farm) {
        return FarmInfoResponseDTO.builder()
                .farmId(farm.getId())
                .farmName(farm.getFarmName())
                .farmRepresentative(farm.getFarmRepresentative())
                .phoneNumber(farm.getPhoneNumber())
                .businessRegistrationNumber(farm.getBusinessRegistrationNumber())
                .address(farm.getAddress())
                .region(farm.getRegion().getName())
                .build();
    }
}
