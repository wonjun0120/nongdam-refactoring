package shop.nongdam.nongdambackend.farm.api.dto.response;

import lombok.Builder;
import shop.nongdam.nongdambackend.farm.domain.FarmBadge;
import shop.nongdam.nongdambackend.farm.domain.FarmBadgeAssignment;

@Builder
public record FarmBadgeResponseDTO (
        Long badgeId,
        String badgeName,
        String badgeImage
){
    public static FarmBadgeResponseDTO from(FarmBadgeAssignment farmBadgeAssignment){
        return FarmBadgeResponseDTO.builder()
                .badgeId(farmBadgeAssignment.getFarmBadge().getId())
                .badgeName(farmBadgeAssignment.getFarmBadge().getName())
                .badgeImage(farmBadgeAssignment.getFarmBadge().getImg())
                .build();
    }
}
