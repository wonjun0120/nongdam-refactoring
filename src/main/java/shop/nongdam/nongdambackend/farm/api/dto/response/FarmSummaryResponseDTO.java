package shop.nongdam.nongdambackend.farm.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.farm.domain.Farm;

@Builder
public record FarmSummaryResponseDTO(
        Long farmId,
        String farmName,
        String profileImage,
        String farmRepresentative,
        String phoneNumber,
        String address,
        String region
) {
    public static FarmSummaryResponseDTO from(Farm farm){
        return FarmSummaryResponseDTO.builder()
                .farmId(farm.getId())
                .farmName(farm.getFarmName())
                .profileImage(farm.getProfileImage())
                .farmRepresentative(farm.getFarmRepresentative())
                .phoneNumber(farm.getPhoneNumber())
                .address(farm.getAddress())
                .region(farm.getRegion().getName())
                .build();
    }
}
