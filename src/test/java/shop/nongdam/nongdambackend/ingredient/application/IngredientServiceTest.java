package shop.nongdam.nongdambackend.ingredient.application;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.auth.exception.EmailNotFoundException;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmRepository;
import shop.nongdam.nongdambackend.farm.exception.FarmAccessDeniedException;
import shop.nongdam.nongdambackend.farm.exception.FarmNotFoundException;
import shop.nongdam.nongdambackend.global.aws.application.ImageService;
import shop.nongdam.nongdambackend.ingredient.api.dto.request.IngredientSaveRequestDTO;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;
import shop.nongdam.nongdambackend.ingredient.domain.IngredientCategory;
import shop.nongdam.nongdambackend.ingredient.domain.IngredientUglyReason;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientCategoryRepository;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientRepository;
import shop.nongdam.nongdambackend.ingredient.domain.repository.IngredientUglyReasonRepository;
import shop.nongdam.nongdambackend.ingredient.exception.IngredientCategoryNotFoundException;
import shop.nongdam.nongdambackend.ingredient.exception.IngredientUglyReasonNotFoundException;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.openai.application.OpenAiService;
import shop.nongdam.nongdambackend.region.domain.Region;
import shop.nongdam.nongdambackend.region.domain.repository.RegionRepository;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    // 테스트 상수
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_FARM_ID = 1L;
    private static final String TEST_INGREDIENT_NAME = "양파";
    private static final String TEST_UGLY_REASON = "모양이 달라요";
    private static final String TEST_CATEGORY = "고추/마늘/양파";
    private static final Long TEST_PRICE = 2500L;

    @InjectMocks private IngredientService ingredientService;

    @Mock private ImageService imageService;
    @Mock private OpenAiService openAiService;
    @Mock private IngredientRepository ingredientRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private FarmRepository farmRepository;
    @Mock private IngredientUglyReasonRepository ingredientUglyReasonRepository;
    @Mock private IngredientCategoryRepository ingredientCategoryRepository;
    @Mock private RegionRepository regionRepository;

    private Member testMember;
    private Farm testFarm;
    private IngredientUglyReason testUglyReason;
    private IngredientCategory testCategory;
    private MultipartFile testImage;

    @BeforeEach
    void setUp() {
        testMember = createTestMember();
        testFarm = createTestFarm(testMember);
        testUglyReason = createTestUglyReason();
        testCategory = createTestCategory(TEST_CATEGORY);
        testImage = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("saveIngredientInfo: 정상적인 요청이 들어오면 재료를 저장하고 응답 DTO를 반환")
    void saveIngredientInfo_withValidRequest_shouldReturnResponseDto() {
        // Given
        IngredientSaveRequestDTO request = createIngredientSaveRequest();
        List<MultipartFile> images = List.of(testImage);
        setupMockRepositoryResponses();

        // When
        var result = ingredientService.saveIngredientInfo(TEST_EMAIL, TEST_FARM_ID, request, images);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.ingredientName()).isEqualTo(TEST_INGREDIENT_NAME);
        verify(ingredientRepository).save(any());
        verify(imageService).saveImage(testImage);
    }

    @Test
    @DisplayName("saveIngredientInfo: 존재하지 않는 이메일일 경우 EmailNotFoundException 발생")
    void givenInvalidEmail_whenSaveIngredientInfo_thenThrowEmailNotFoundException() {
        // given
        IngredientSaveRequestDTO request = createIngredientSaveRequest();
        List<MultipartFile> images = List.of(testImage);

        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                ingredientService.saveIngredientInfo(TEST_EMAIL, TEST_FARM_ID, request, images)
        ).isInstanceOf(EmailNotFoundException.class);

        verify(memberRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("saveIngredientInfo: 요청한 farmId가 멤버의 실제 농장과 다르면 FarmAccessDeniedException 발생")
    void givenWrongFarmId_whenSaveIngredientInfo_thenThrowFarmAccessDeniedException() {
        // given
        IngredientSaveRequestDTO request = createIngredientSaveRequest();
        List<MultipartFile> images = List.of(testImage);
        long wrongFarmId = 999L;

        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.of(testFarm));

        // when & then
        assertThatThrownBy(() ->
                ingredientService.saveIngredientInfo(TEST_EMAIL, wrongFarmId, request, images)
        ).isInstanceOf(FarmAccessDeniedException.class);
    }

    @Test
    @DisplayName("saveIngredientInfo: 존재하지 않는 못생김 사유일 경우 IngredientUglyReasonNotFoundException 발생")
    void givenInvalidUglyReason_whenSaveIngredientInfo_thenThrowIngredientUglyReasonNotFoundException() {
        // given
        IngredientSaveRequestDTO request = new IngredientSaveRequestDTO(
                TEST_INGREDIENT_NAME, TEST_CATEGORY, "없는이유", "신선한 양파입니다", TEST_PRICE
        );
        List<MultipartFile> images = List.of(testImage);

        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.of(testFarm));
        when(ingredientUglyReasonRepository.findByName("없는이유")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                ingredientService.saveIngredientInfo(TEST_EMAIL, TEST_FARM_ID, request, images)
        ).isInstanceOf(IngredientUglyReasonNotFoundException.class);
    }

    @Test
    @DisplayName("saveIngredientInfo: 회원에게 농장이 존재하지 않으면 FarmNotFoundException 발생")
    void givenNoFarm_whenSaveIngredientInfo_thenThrowFarmNotFoundException() {
        // given
        IngredientSaveRequestDTO request = createIngredientSaveRequest();
        List<MultipartFile> images = List.of(testImage);

        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                ingredientService.saveIngredientInfo(TEST_EMAIL, TEST_FARM_ID, request, images)
        ).isInstanceOf(FarmNotFoundException.class);
    }

    @Test
    @DisplayName("saveIngredientInfo: 존재하지 않는 카테고리일 경우 IngredientCategoryNotFoundException 발생")
    void givenInvalidCategory_whenSaveIngredientInfo_thenThrowIngredientCategoryNotFoundException() {
        // given
        IngredientSaveRequestDTO request = new IngredientSaveRequestDTO(
                TEST_INGREDIENT_NAME, "없는카테고리", TEST_UGLY_REASON,"설명", TEST_PRICE
        );
        List<MultipartFile> images = List.of(testImage);

        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.of(testFarm));
        when(ingredientUglyReasonRepository.findByName(TEST_UGLY_REASON)).thenReturn(Optional.of(testUglyReason));
        when(ingredientCategoryRepository.findByName("없는카테고리")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                ingredientService.saveIngredientInfo(TEST_EMAIL, TEST_FARM_ID, request, images)
        ).isInstanceOf(IngredientCategoryNotFoundException.class);
    }

    @Test
    @DisplayName("findAll: category=전체, region=전체일 경우 전체 결과 반환")
    void findAll_withAllCategoryAndAllRegion_shouldReturnAllIngredients() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        Page<Ingredient> mockPage = new PageImpl<>(List.of(ingredient1, ingredient2));

        when(ingredientRepository.findAllIngredients(pageable)).thenReturn(mockPage);

        when(ingredient1.getIngredientCategory()).thenReturn(testCategory);
        when(ingredient1.getIngredientUglyReason()).thenReturn(testUglyReason);
        when(ingredient1.getFarm()).thenReturn(testFarm);

        when(ingredient2.getIngredientCategory()).thenReturn(testCategory);
        when(ingredient2.getIngredientUglyReason()).thenReturn(testUglyReason);
        when(ingredient2.getFarm()).thenReturn(testFarm);

        // when
        var result = ingredientService.findAll("전체", "전체", pageable);

        // then
        assertThat(result.ingredientInfoResponseDTOs()).hasSize(2);
        verify(ingredientRepository).findAllIngredients(pageable);
    }

    @Test
    @DisplayName("findAll: category=고추/마늘/양파, region=전체 -> category 필터링 적용")
    void findAll_withCategoryOnly_shouldFilterByCategory() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Ingredient ingredient1 = mock(Ingredient.class);
        Ingredient ingredient2 = mock(Ingredient.class);
        Page<Ingredient> mockPage = new PageImpl<>(List.of(ingredient1, ingredient2));

        when(ingredientRepository.findAllIngredients(pageable)).thenReturn(mockPage);

        when(ingredient1.getIngredientCategory()).thenReturn(testCategory);
        when(ingredient1.getIngredientUglyReason()).thenReturn(testUglyReason);
        when(ingredient1.getFarm()).thenReturn(testFarm);

        IngredientCategory anotherCategory = createTestCategory("감자/당근/고구마");
        when(ingredient2.getIngredientCategory()).thenReturn(anotherCategory);

        when(ingredientCategoryRepository.findByName(testCategory.getName())).thenReturn(Optional.of(testCategory));

        // when
        var result = ingredientService.findAll(testCategory.getName(), "전체", pageable);

        // then
        assertThat(result.ingredientInfoResponseDTOs()).hasSize(1);
        verify(ingredientRepository).findAllIngredients(pageable);
    }

    @Test
    @DisplayName("findAll: category=전체, region=서울 → region 필터링 적용")
    void findAll_withRegionOnly_shouldFilterByRegion() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Region matchedRegion = mock(Region.class);
        when(matchedRegion.getName()).thenReturn("서울");

        Region otherRegion = mock(Region.class);

        Farm farm1 = mock(Farm.class);
        when(farm1.getRegion()).thenReturn(matchedRegion);
        when(farm1.getId()).thenReturn(1L);

        Farm farm2 = mock(Farm.class);
        when(farm2.getRegion()).thenReturn(otherRegion);

        Ingredient ingredient1 = mock(Ingredient.class);
        when(ingredient1.getFarm()).thenReturn(farm1);
        when(ingredient1.getIngredientCategory()).thenReturn(testCategory);
        when(ingredient1.getIngredientUglyReason()).thenReturn(testUglyReason);

        Ingredient ingredient2 = mock(Ingredient.class);
        when(ingredient2.getFarm()).thenReturn(farm2);

        Page<Ingredient> page = new PageImpl<>(List.of(ingredient1, ingredient2));

        when(ingredientRepository.findAllIngredients(pageable)).thenReturn(page);
        when(regionRepository.findByName("서울")).thenReturn(Optional.of(matchedRegion));

        // when
        var result = ingredientService.findAll("전체", "서울", pageable);

        // then
        assertThat(result.ingredientInfoResponseDTOs()).hasSize(1); // only ingredient1
        verify(regionRepository).findByName("서울");
    }

    @Test
    @DisplayName("findById: 존재하는 재료 ID 조회 시 응답 DTO 반환")
    void findById_withValidId_shouldReturnIngredientInfoResponseDTO() {
        // given
        Long ingredientId = 1L;

        Ingredient mockIngredient = mock(Ingredient.class);
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(mockIngredient));
        when(mockIngredient.getIngredientCategory()).thenReturn(testCategory);
        when(mockIngredient.getIngredientUglyReason()).thenReturn(testUglyReason);
        when(mockIngredient.getFarm()).thenReturn(testFarm);

        // when
        var result = ingredientService.findById(ingredientId);

        // then
        assertThat(result).isNotNull();
        verify(ingredientRepository).findById(ingredientId);
    }

    @Test
    @DisplayName("chatGpt: 이미 gptComment가 존재하는 경우 기존 값 반환")
    void chatGpt_whenGptCommentExists_shouldReturnExistingComment() {
        // given
        Long ingredientId = 1L;
        String existingComment = "기존 GPT 코멘트입니다.";

        Ingredient ingredient = mock(Ingredient.class);
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredient.getGptComment()).thenReturn(existingComment);

        // when
        var result = ingredientService.chatGpt(ingredientId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo(existingComment);

        verify(ingredientRepository).findById(ingredientId);
        verifyNoInteractions(openAiService);
    }

    @Test
    @DisplayName("chatGpt: gptComment가 없으면 GPT 호출 후 저장")
    void chatGpt_whenGptCommentIsBlank_shouldCallOpenAiAndSave() {
        // given
        Long ingredientId = 1L;
        String generatedComment = "GPT가 생성한 코멘트입니다.";

        Region region = mock(Region.class);
        when(region.getName()).thenReturn("서울");

        Farm farm = mock(Farm.class);
        when(farm.getRegion()).thenReturn(region);

        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getGptComment()).thenReturn(""); // 빈 문자열
        when(ingredient.getFarm()).thenReturn(farm);
        when(ingredient.getIngredientName()).thenReturn("양파");

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(openAiService.chat(anyString())).thenReturn(generatedComment);

        // when
        var result = ingredientService.chatGpt(ingredientId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo(generatedComment);

        verify(openAiService).chat(anyString());
        verify(ingredient).setGptComment(generatedComment);
        verify(ingredientRepository).save(ingredient);
    }


    private Member createTestMember() {
        return Member.builder()
                .email(TEST_EMAIL)
                .name("테스터")
                .picture("test.jpg")
                .socialType(SocialType.KAKAO)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .certificate("test")
                .build();
    }

    private Farm createTestFarm(Member member) {
        Region region = mock(Region.class);
        Farm farm = Farm.builder()
                .member(member)
                .farmName("테스트 농장")
                .profileImage("test.jpg")
                .farmRepresentative("테스터")
                .phoneNumber("010-1234-5678")
                .businessRegistrationNumber("123-45-67890")
                .address("서울시 강남구")
                .region(region)
                .latitude(37.1234)
                .longitude(127.1234)
                .build();
        ReflectionTestUtils.setField(farm, "id", TEST_FARM_ID);
        return farm;
    }

    private IngredientUglyReason createTestUglyReason() {
        try {
            Constructor<IngredientUglyReason> constructor = IngredientUglyReason.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            IngredientUglyReason reason = constructor.newInstance();
            ReflectionTestUtils.setField(reason, "id", 1L);
            ReflectionTestUtils.setField(reason, "name", TEST_UGLY_REASON);
            return reason;
        } catch (Exception e) {
            throw new RuntimeException("IngredientUglyReason 생성 실패", e);
        }
    }

    private IngredientCategory createTestCategory(String categoryName) {
        try {
            Constructor<IngredientCategory> constructor = IngredientCategory.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            IngredientCategory category = constructor.newInstance();
            ReflectionTestUtils.setField(category, "id", 1L);
            ReflectionTestUtils.setField(category, "name", categoryName);
            return category;
        } catch (Exception e) {
            throw new RuntimeException("IngredientCategory 생성 실패", e);
        }
    }

    private IngredientSaveRequestDTO createIngredientSaveRequest() {
        return new IngredientSaveRequestDTO(
                TEST_INGREDIENT_NAME,
                TEST_CATEGORY,
                TEST_UGLY_REASON,
                "신선한 양파입니다",
                TEST_PRICE
        );
    }

    private void setupMockRepositoryResponses() {
        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.of(testFarm));
        when(ingredientUglyReasonRepository.findByName(TEST_UGLY_REASON)).thenReturn(Optional.of(testUglyReason));
        when(ingredientCategoryRepository.findByName(TEST_CATEGORY)).thenReturn(Optional.of(testCategory));
        when(imageService.saveImage(testImage)).thenReturn("https://example.com/test.jpg");
        when(ingredientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }
}
