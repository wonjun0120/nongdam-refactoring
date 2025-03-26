package shop.nongdam.nongdambackend.restaurant.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.annotation.AuthorizeRole;
import shop.nongdam.nongdambackend.global.annotation.CurrentMemberEmail;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantDetailSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantDetailInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantTagResponseDTOs;
import shop.nongdam.nongdambackend.restaurant.appllication.RestaurantService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController implements RestaurantDocs {
    private final RestaurantService restaurantService;

    @Override
    @PostMapping
    public ApiResponseTemplate<RestaurantInfoResponseDTO> save(@CurrentMemberEmail String email,
                                                               @Valid @RequestBody RestaurantSaveRequestDTO restaurantSaveRequestDTO) {
        return ApiResponseTemplate.created("식당 등록 성공.", restaurantService.save(email, restaurantSaveRequestDTO));
    }

    @Override
    @PatchMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthorizeRole({Role.ROLE_ADMIN, Role.ROLE_RESTAURANT})
    public ApiResponseTemplate<RestaurantDetailInfoResponseDTO> registerRestaurantDetail(
            @CurrentMemberEmail String email,
            @Valid @RequestPart RestaurantDetailSaveRequestDTO restaurantDetailSaveRequestDTO,
            @RequestPart(value = "restaurantImage", required = false) MultipartFile restaurantImage) {
        return ApiResponseTemplate.ok("식당 상세 정보 등록 성공",
                restaurantService.registerRestaurantDetail(email, restaurantDetailSaveRequestDTO, restaurantImage));
    }

    @Override
    @GetMapping("/{restaurantId}")
    public ApiResponseTemplate<RestaurantDetailInfoResponseDTO> findById(@PathVariable Long restaurantId) {
        return ApiResponseTemplate.ok("식당 상세 조회 성공", restaurantService.findById(restaurantId));
    }

    @Override
    @GetMapping
    public ApiResponseTemplate<RestaurantTagResponseDTOs> findAll(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {
        return ApiResponseTemplate.ok("식당 전체 조회 성공", restaurantService.findAll(PageRequest.of(page, size)));
    }

    @Override
    @DeleteMapping("/{restaurantId}")
    @AuthorizeRole({Role.ROLE_ADMIN, Role.ROLE_RESTAURANT})
    public ApiResponseTemplate<Void> delete(@CurrentMemberEmail String email, @PathVariable Long restaurantId) {
        restaurantService.deleteById(email, restaurantId);
        return ApiResponseTemplate.ok("식당 삭제 성공");
    }
}
