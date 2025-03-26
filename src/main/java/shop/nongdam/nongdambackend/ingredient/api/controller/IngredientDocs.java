package shop.nongdam.nongdambackend.ingredient.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.ingredient.api.dto.request.IngredientSaveRequestDTO;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientInfoResponseDTO;
import shop.nongdam.nongdambackend.ingredient.api.dto.response.IngredientInfoResponseDTOs;

import java.util.List;

@Tag(name = "[식료품 API]", description = "식료품 관련 API")
public interface IngredientDocs {

    @Operation(summary = "식료품 등록", description = "농산물 판매자가 식료품을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "식료품 등록 성공",
                            content = @Content(schema = @Schema(implementation = IngredientInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "403", description = "인증된 판매자가 아님, 권한이 없는 판매처 접근"),
                    @ApiResponse(responseCode = "404", description = "식료품 카테고리, 못난이 이유 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<IngredientInfoResponseDTO> save(
            @Parameter(description = "농산물 판매자(농가) ID", required = true) Long farmId,
            @Parameter(hidden = true) String email,
            @Parameter(description = "식료품 정보", required = true) IngredientSaveRequestDTO ingredientSaveRequestDto,
            @Parameter(description = "식료품 이미지", required = true) List<MultipartFile> ingredientImages
    );

    @Operation(summary = "식료품 상세 조회", description = "식료품 ID로 식료품 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식료품 상세 조회 성공",
                            content = @Content(schema = @Schema(implementation = IngredientInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "식료품 ID로 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<IngredientInfoResponseDTO> findById(
            @Parameter(description = "식료품 ID", required = true) Long ingredientId
    );

    @Operation(summary = "식료품 전체 조회", description = "식료품 카테고리, 지역으로 식료품 전체 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식료품 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = IngredientInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "식료품 카테고리, 지역을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<IngredientInfoResponseDTOs> findAll(
            @Parameter(description = "식료품 카테고리", required = true) String category,
            @Parameter(description = "식료품 지역", required = true) String region,
            @Parameter(description = "페이지 번호", required = true) int page,
            @Parameter(description = "페이지 크기", required = true) int size
    );
}
