package shop.nongdam.nongdambackend.farm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import shop.nongdam.nongdambackend.farm.api.dto.request.FarmSaveRequestDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmDetailInfoResponseDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmInfoResponseDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmInfoResponseDTOs;
import shop.nongdam.nongdambackend.global.annotation.CurrentMemberEmail;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;

@Tag(name = "[판매자 API]", description = "농산물 판매자(농가) 관련 API")
public interface FarmDocs {
    @Operation(summary = "농산물 판매자 등록", description = "카카오 소셜 회원가입 후 농산물 판매자를 등록합니다.",
        responses = {
                @ApiResponse(responseCode = "201", description = "농산물 판매자 등록 성공",
                    content = @Content(schema = @Schema(implementation = FarmInfoResponseDTO.class))),
                @ApiResponse(responseCode = "401", description = "인증 실패"),
                @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
        }
    )
    ApiResponseTemplate<FarmInfoResponseDTO> save(
            @Parameter(hidden = true) @CurrentMemberEmail String email,
            @Parameter(description = "농산물 판매자 정보", required = true) @RequestBody FarmSaveRequestDTO farmSaveRequestDTO
    );

    @Operation(summary = "농산물 판매자 상세 조회", description = "농산물 판매자 ID로 상세 조회를 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "농산물 판매자 조회 성공",
                            content = @Content(schema = @Schema(implementation = FarmInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "농산물 판매자 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<FarmDetailInfoResponseDTO> findById(
            @Parameter(description = "농산물 판매자 ID", required = true) Long farmId
    );

    @Operation(summary = "농산물 판매자 전체 조회", description = "농산물 판매자 전체 조회를 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "농산물 판매자 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = FarmInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<FarmInfoResponseDTOs> findAll(
            @Parameter(description = "페이지 번호", required = true) int page,
            @Parameter(description = "한 페이지 크기", required = true) int size
    );

    @Operation(summary = "농산물 판매자 뱃지 부여", description = "농산물 판매자에게 뱃지를 부여합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "농산물 판매자 뱃지 부여 성공",
                            content = @Content(schema = @Schema(implementation = FarmDetailInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "농산물 판매자 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<FarmDetailInfoResponseDTO> giveBadge(
            @Parameter(description = "농산물 판매자 ID", required = true) Long farmId,
            @Parameter(description = "뱃지 이름", required = true) String badgeName
    );
}
