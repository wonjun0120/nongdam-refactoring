package shop.nongdam.nongdambackend.farm.api.dto.response;

import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;

import java.util.List;

public record FarmInfoResponseDTOs(
        List<FarmInfoResponseDTO> farmInfoResponseDTOS,
        PageInfoResDto pageInfoResDto
) {
    public static FarmInfoResponseDTOs of(
            List<FarmInfoResponseDTO> farmInfoResponseDTOS,
            PageInfoResDto pageInfoResDto
    ){
        return new FarmInfoResponseDTOs(farmInfoResponseDTOS, pageInfoResDto);
    }
}
