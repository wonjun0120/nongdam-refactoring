package shop.nongdam.nongdambackend.menu.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.aws.application.ImageService;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.member.exception.MemberNotFoundException;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;
import shop.nongdam.nongdambackend.restaurant.domain.repository.RestaurantRepository;
import shop.nongdam.nongdambackend.restaurant.exception.AccessDeniedRestaurantException;
import shop.nongdam.nongdambackend.restaurant.exception.RestaurantNotFoundException;
import shop.nongdam.nongdambackend.restaurant.menu.api.dto.request.MenuSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.menu.application.MenuService;
import shop.nongdam.nongdambackend.restaurant.menu.domain.Menu;
import shop.nongdam.nongdambackend.restaurant.menu.domain.repository.MenuRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks private MenuService menuService;

    @Mock private MenuRepository menuRepository;
    @Mock private ImageService imageService;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private MemberRepository memberRepository;

    private static final String EMAIL = "test@user.com";

    private Member testMember;
    private Restaurant testRestaurant;
    private MultipartFile menuImage;
    private MultipartFile farmProduceImage;

    @BeforeEach
    void setUp() {
        testMember = createTestMember();

        testRestaurant = createTestRestaurant(testMember, createTestMenu());
        menuImage = mock(MultipartFile.class);
        farmProduceImage = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("save: 메뉴 등록에 성공하면 MenuInfoResponseDTO를 반환한다.")
    void saveMenu_withValidRequest_shouldReturnMenuInfoResponseDTO() {
        // given
        MenuSaveRequestDTO requestDTO = new MenuSaveRequestDTO(
                testRestaurant.getId(),
                "테스트 메뉴",
                10000,
                "농장 농산물",
                "메인 설명",
                "서브 설명",
                true
        );

        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testMember));
        when(restaurantRepository.findById(testRestaurant.getId())).thenReturn(Optional.of(testRestaurant));
        when(imageService.saveImage(menuImage)).thenReturn("menuImage");
        when(imageService.saveImage(farmProduceImage)).thenReturn("farmProduceImage");
        when(menuRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        var result = menuService.save(
                testMember.getEmail(),
                requestDTO,
                menuImage,
                farmProduceImage
        );

        // then
        assertThat(result.name()).isEqualTo("테스트 메뉴");
        verify(menuRepository).save(any());
        verify(imageService, times(2)).saveImage(any());
        verify(imageService).saveImage(menuImage);
        verify(imageService).saveImage(farmProduceImage);
    }

    @Test
    @DisplayName("save: 존재하지 않는 회원의 이메일로 메뉴 등록 시 MemberNotFoundException을 던진다.")
    void saveMenu_withInvalidMemberEmail_shouldThrowMemberNotFoundException() {
        // given
        MenuSaveRequestDTO requestDTO = new MenuSaveRequestDTO(
                testRestaurant.getId(),
                "테스트 메뉴",
                10000,
                "농장 농산물",
                "메인 설명",
                "서브 설명",
                true
        );

        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.save(
                testMember.getEmail(),
                requestDTO,
                menuImage,
                farmProduceImage
        )).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("save: 존재하지 않는 식당 ID로 메뉴 등록 시 RestaurantNotFoundException을 던진다.")
    void saveMenu_withInvalidRestaurantId_shouldThrowRestaurantNotFoundException() {
        // given
        MenuSaveRequestDTO requestDTO = new MenuSaveRequestDTO(
                testRestaurant.getId(),
                "테스트 메뉴",
                10000,
                "농장 농산물",
                "메인 설명",
                "서브 설명",
                true
        );

        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testMember));
        when(restaurantRepository.findById(testRestaurant.getId())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.save(
                testMember.getEmail(),
                requestDTO,
                menuImage,
                farmProduceImage
        )).isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    @DisplayName("save: 레스토랑 소유자가 아닌 회원이 메뉴 등록 시 AccessDeniedRestaurantException을 던진다.")
    void saveMenu_withDifferentOwner_shouldThrowAccessDeniedRestaurantException() {
        // given
        MenuSaveRequestDTO requestDTO = new MenuSaveRequestDTO(
                testRestaurant.getId(),
                "테스트 메뉴",
                10000,
                "농장 농산물",
                "메인 설명",
                "서브 설명",
                true
        );

        Member differentMember = createTestMember();
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(differentMember));
        when(restaurantRepository.findById(testRestaurant.getId())).thenReturn(Optional.of(testRestaurant));

        // when, then
        assertThatThrownBy(() -> menuService.save(
                testMember.getEmail(),
                requestDTO,
                menuImage,
                farmProduceImage
        )).isInstanceOf(AccessDeniedRestaurantException.class);
    }

    private Member createTestMember() {
        return Member.builder()
                .email(EMAIL)
                .name("홍길동")
                .picture("picture")
                .socialType(SocialType.KAKAO)
                .role(Role.ROLE_USER)
                .firstLogin(false)
                .certificate("certificate")
                .build();
    }

    private Menu createTestMenu() {
        return Menu.builder()
                .name("메뉴")
                .price(10000)
                .image("image")
                .farmProduce("농산물")
                .farmProduceImage("farmImage")
                .mainDescription("메인 설명")
                .subDescription("서브 설명")
                .isMainMenu(true)
                .build();
    }

    private Restaurant createTestRestaurant(Member member, Menu menu) {
        return Restaurant.builder()
                .restaurantName("테스트식장")
                .restaurantRepresentative("설명")
                .member(member)
                .phoneNumber("010-1234-5678")
                .businessRegistrationNumber("123-45-67890")
                .address("서울시 강남구 테스트동")
                .latitude(37.123456)
                .longitude(127.123456)
                .restaurantImage("image")
                .openTime("09:00")
                .closeTime("21:00")
                .precautions("주의사항")
                .isRegistered(true)
                .menu(List.of(menu))
                .build();
    }

}
