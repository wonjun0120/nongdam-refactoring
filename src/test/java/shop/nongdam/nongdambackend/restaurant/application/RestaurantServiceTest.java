package shop.nongdam.nongdambackend.restaurant.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.aws.application.ImageService;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantDetailSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;
import shop.nongdam.nongdambackend.restaurant.domain.repository.RestaurantRepository;
import shop.nongdam.nongdambackend.restaurant.exception.AccessDeniedRestaurantException;
import shop.nongdam.nongdambackend.restaurant.exception.RestaurantNotFoundException;
import shop.nongdam.nongdambackend.restaurant.menu.domain.Menu;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @InjectMocks private RestaurantService restaurantService;

    @Mock private MemberRepository memberRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private ImageService imageService;


    private final String EMAIL = "test@test.com";
    private Member member;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        member = createTestMember();
        Menu menu = createTestMenu();
        restaurant = createTestRestaurant(member, menu);
    }

    @Test
    @DisplayName("save: 정상적으로 레스토랑 등록")
    void save_withValidateRequest_shouldReturnDTO() {
        // given
        RestaurantSaveRequestDTO requestDTO = new RestaurantSaveRequestDTO(
                "name",
                "홍길동",
                "010-1234-5678",
                "123-45-67890",
                "서울시 강남구 테스트동"
        );
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(member));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // when
        var result = restaurantService.save(EMAIL, requestDTO);

        // then
        assertThat(result.restaurantName()).isEqualTo("name");
        assertThat(member.getRole()).isEqualTo(Role.ROLE_RESTAURANT);
        verify(memberRepository).findByEmail(EMAIL);
        verify(restaurantRepository).save(any(Restaurant.class));

    }

    @Test
    @DisplayName("findById: 존재하는 ID 정상 조회")
    void findById_withValidId_shouldReturnDTO() {
        // given
        Long id = 1L;
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        // when
        var result = restaurantService.findById(id);

        // then
        assertThat(result.restaurantName()).isEqualTo(restaurant.getRestaurantName());
        verify(restaurantRepository).findById(id);
    }

    @Test
    @DisplayName("findById: 존재하지 않는 ID 조회시 예외 발생")
    void findById_withInvalidId_shouldThrowException() {
        // given
        Long invalidId = 1L;
        when(restaurantRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> restaurantService.findById(invalidId))
                .isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById: 정상적으로 레스토랑 삭제")
    void deleteById_withValidId_shouldDeleteRestaurant() {
        // given
        Long id = 1L;
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(member));
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        // when
        restaurantService.deleteById(EMAIL, id);

        // then
        verify(memberRepository).findByEmail(EMAIL);
        verify(restaurantRepository).findById(id);
        assertThat(restaurant.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("deleteById: 존재하지 않는 ID 삭제시 예외 발생")
    void deleteById_withInvalidId_shouldThrowException() {
        // given
        Long invalidId = 2L;
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(member));
        when(restaurantRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> restaurantService.deleteById(EMAIL, invalidId))
                .isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById: 다른 사용자의 레스토랑 삭제시 예외 발생")
    void deleteById_withDifferentOwner_shouldThrowException() {
        // given
        Long id = 1L;
        Member differentMember = createTestMember();
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(differentMember));
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        // when, then
        assertThatThrownBy(() -> restaurantService.deleteById(EMAIL, id))
                .isInstanceOf(AccessDeniedRestaurantException.class);
    }

    @Test
    @DisplayName("findAll: 정상적으로 레스토랑 목록 조회")
    void findAll_withValidPageable_shouldReturnDTO() {
        // given
        Menu menu = Menu.builder()
                .isMainMenu(true)
                .name("메뉴")
                .price(10000)
                .image("image")
                .farmProduce("농산물")
                .farmProduceImage("farmImage")
                .mainDescription("메인 설명")
                .subDescription("서브 설명")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Restaurant restaurant1 = createTestRestaurant(member, menu);
        Restaurant restaurant2 = createTestRestaurant(member,  menu);
        Page<Restaurant> mockPage = new PageImpl<>(List.of(restaurant1, restaurant2));

        when(restaurantRepository.findRegisteredRestaurants(pageable)).thenReturn(mockPage);

        // when
        var result = restaurantService.findAll(pageable);

        // then
        assertThat(result.restaurantTagResponseDTOs()).hasSize(2);
        verify(restaurantRepository).findRegisteredRestaurants(pageable);
    }

    @Test
    @DisplayName("registerRestaurantDetail: 정상적으로 레스토랑 상세 정보 등록")
    void registerRestaurantDetail_withValidRequest_shouldReturnDTO() {
        // given
        RestaurantDetailSaveRequestDTO requestDTO = new RestaurantDetailSaveRequestDTO(
                1L,
                37.123456,
                127.123456,
                "09:00",
                "21:00",
                "주의사항"
        );
        MultipartFile restaurantImage = null;
        when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(member));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        // when
        var result = restaurantService.registerRestaurantDetail(EMAIL, requestDTO, restaurantImage);

        // then
        assertThat(result.restaurantName()).isEqualTo(restaurant.getRestaurantName());
        verify(memberRepository).findByEmail(EMAIL);
        verify(restaurantRepository).findById(1L);
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
