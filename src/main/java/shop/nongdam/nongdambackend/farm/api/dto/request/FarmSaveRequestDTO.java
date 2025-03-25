package shop.nongdam.nongdambackend.farm.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FarmSaveRequestDTO(
        @NotBlank(message = "농가 이름은 필수 입력값입니다.")
        String farmName,
        @NotBlank(message = "농가 대표자는 필수 입력값입니다.")
        String farmRepresentative,
        @NotBlank(message = "농가 전화번호는 필수 입력값입니다.")
        String phoneNumber,
        @NotBlank(message = "농가관련 사업자등록번호는 필수 입력값입니다.")
        String businessRegistrationNumber,
        @NotBlank(message = "주소는 필수 입력값입니다.")
        String address
) {
}
