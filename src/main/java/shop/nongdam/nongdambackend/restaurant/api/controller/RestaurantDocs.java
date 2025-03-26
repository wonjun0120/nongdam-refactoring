package shop.nongdam.nongdambackend.restaurant.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantDetailSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantDetailInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantInfoResponseDTOs;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantTagResponseDTOs;


@Tag(name = "[식당 API]", description = "식당 관련 API")
public interface RestaurantDocs {

    @Operation(summary = "식당 등록", description = "새로운 식당 정보를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "식당 등록 성공",
                            content = @Content(schema = @Schema(implementation = RestaurantInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<RestaurantInfoResponseDTO> save(
            @Parameter(hidden = true) String email,
            @Parameter(description = "식당 저장 요청 데이터", required = true,
                    schema = @Schema(implementation = RestaurantSaveRequestDTO.class))
            RestaurantSaveRequestDTO restaurantSaveRequestDTO);

    @Operation(summary = "식당 상세 정보 등록", description = "식당의 상세 정보를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식당 상세 정보 등록 성공",
                            content = @Content(schema = @Schema(implementation = RestaurantDetailInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<RestaurantDetailInfoResponseDTO> registerRestaurantDetail(
            @Parameter(hidden = true) String email,
            @Parameter(description = "메뉴 저장 요청 데이터", required = true,
                    schema = @Schema(implementation = RestaurantDetailSaveRequestDTO.class))
            RestaurantDetailSaveRequestDTO restaurantDetailSaveRequestDTO,
            @RequestPart(value = "menuImage", required = false) MultipartFile restaurantImage
    );

    @Operation(summary = "식당 상세 조회", description = "ID로 특정 식당의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식당 상세 조회 성공",
                            content = @Content(schema = @Schema(implementation = RestaurantInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<RestaurantDetailInfoResponseDTO> findById(
            @Parameter(description = "식당 ID", required = true) Long restaurantId);

    @Operation(summary = "식당 전체 조회", description = "모든 식당 정보를 페이징하여 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식당 전체 조회 성공",
                            content = @Content(schema = @Schema(implementation = RestaurantInfoResponseDTOs.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<RestaurantTagResponseDTOs> findAll(
            @Parameter(description = "페이지 번호", required = false, example = "0") int page,
            @Parameter(description = "페이지 크기", required = false, example = "10") int size);

    @Operation(summary = "식당 삭제", description = "ID로 특정 식당을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "식당 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<Void> delete(
            @Parameter(hidden = true) String email,
            @Parameter(description = "식당 ID", required = true) Long restaurantId);
}
