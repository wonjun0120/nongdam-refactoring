package shop.nongdam.nongdambackend.restaurant.menu.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.annotation.AuthorizeRole;
import shop.nongdam.nongdambackend.global.annotation.CurrentMemberEmail;
import shop.nongdam.nongdambackend.global.template.ApiResponseTemplate;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.restaurant.menu.api.dto.request.MenuSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.menu.api.dto.response.MenuInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.menu.application.MenuService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController implements MenuDocs {
    private final MenuService menuService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthorizeRole({Role.ROLE_ADMIN, Role.ROLE_RESTAURANT})
    public ApiResponseTemplate<MenuInfoResponseDTO> save(@CurrentMemberEmail String email,
                                                         @Valid @RequestPart MenuSaveRequestDTO menuSaveRequestDTO,
                                                         @RequestPart(required = false) MultipartFile menuImage,
                                                         @RequestPart(required = false) MultipartFile farmProduceImage) {

        return ApiResponseTemplate.created("메뉴 저장 성공", menuService.save(email, menuSaveRequestDTO, menuImage, farmProduceImage));
    }
}
