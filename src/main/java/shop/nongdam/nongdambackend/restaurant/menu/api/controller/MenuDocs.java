package shop.nongdam.nongdambackend.restaurant.menu.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.menu.api.dto.request.MenuSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.menu.api.dto.response.MenuInfoResponseDTO;

public interface MenuDocs {

    @Operation(summary = "식당 메뉴 등록", description = "새로운 식당 메뉴를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "식당 등록 성공",
                            content = @Content(schema = @Schema(implementation = RestaurantInfoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<MenuInfoResponseDTO> save(
            @Parameter(hidden = true) String email,
            @Parameter(description = "메뉴 저장 요청 데이터", required = true,
                    schema = @Schema(implementation = MenuSaveRequestDTO.class))
            MenuSaveRequestDTO menuSaveRequestDTO,
            @RequestPart(value = "menuImage", required = false) MultipartFile multipartFile,
            @RequestPart(value = "farmProduceImage", required = false) MultipartFile farmProduceImage);
}
