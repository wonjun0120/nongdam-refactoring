package shop.nongdam.nongdambackend.Farm.application;

import org.apache.tomcat.util.bcel.Const;
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
import shop.nongdam.nongdambackend.farm.api.dto.request.FarmSaveRequestDTO;
import shop.nongdam.nongdambackend.farm.application.FarmService;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.farm.domain.FarmBadge;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmBadgeRepository;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmRepository;
import shop.nongdam.nongdambackend.farm.exception.FarmAlreadyExistException;
import shop.nongdam.nongdambackend.farm.exception.FarmBadgeNotFoundException;
import shop.nongdam.nongdambackend.farm.exception.FarmNotFoundException;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.SocialType;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.region.domain.Region;
import shop.nongdam.nongdambackend.region.domain.repository.RegionRepository;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FarmServiceTest {

    private static final String TEST_FARM_NAME = "테스트 농장";
    private static final String TEST_FARM_REPRESENTATIVE = "김테스트";
    private static final String TEST_PHONE_NUMBER = "010-1234-5678";
    private static final String TEST_BUSINESS_REGISTRATION_NUMBER = "123-45-67890";
    private static final String TEST_ADDRESS = "서울시 강남구 테스트로 123";
    private static final String TEST_EMAIL = "test@tester.com";

    @InjectMocks private FarmService farmService;

    @Mock private FarmRepository farmRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private FarmBadgeRepository farmBadgeRepository;
    @Mock private RegionRepository regionRepository;

    @Mock private Member testMember;
    private FarmBadge originCertificationBadge;
    private Region testFarmRegion;

    @BeforeEach
    void setUp(){
        originCertificationBadge = createOriginCertificationBadge();
        testFarmRegion = createTestFarmRegion();
    }

    @Test
    @DisplayName("saveFarmInfo: 정상적인 요청이 들어오면 농장정보를 저장하고 응답 DTO를 반환")
    void saveFarmInfo_withValidRequest_shouldReturnFarmInfoResponseDTO() {
        // given
        FarmSaveRequestDTO request = createFarmSaveRequestDTO();
        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmBadgeRepository.findByName("원산지 인증")).thenReturn(Optional.of(originCertificationBadge));
        when(regionRepository.findByName("서울")).thenReturn(Optional.of(testFarmRegion));

        // when
        var result = farmService.saveFarmInfo(TEST_EMAIL, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.farmName()).isEqualTo(TEST_FARM_NAME);
        verify(farmRepository).save(any());
        verify(testMember).updateRole(Role.ROLE_PRODUCER);
    }

    @Test
    @DisplayName("saveFarmInfo: 이미 농장이 존재하는 회원이 요청을 보내면 예외를 던진다")
    void saveFarmInfo_withAlreadyExistFarm_shouldThrowFarmAlreadyExistException() {
        // given
        FarmSaveRequestDTO request = createFarmSaveRequestDTO();
        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmRepository.findByMemberId(testMember.getId())).thenReturn(Optional.of(mock(Farm.class)));

        // when & then
        assertThatThrownBy(() -> farmService.saveFarmInfo(TEST_EMAIL, request))
                .isInstanceOf(FarmAlreadyExistException.class);
    }

    @Test
    @DisplayName("saveFarmInfo: 농장 뱃지를 찾을 수 없으면 예외를 던진다")
    void saveFarmInfo_withNotFoundFarmBadge_shouldThrowFarmBadgeNotFoundException() {
        // given
        FarmSaveRequestDTO request = createFarmSaveRequestDTO();
        when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
        when(farmBadgeRepository.findByName("원산지 인증")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> farmService.saveFarmInfo(TEST_EMAIL, request))
                .isInstanceOf(FarmBadgeNotFoundException.class);
    }

    @Test
    @DisplayName("findById: 농장 ID로 농장 정보를 조회하고 응답 DTO를 반환")
    void findById_withValidFarmId_shouldReturnFarmDetailInfoResponseDTO() {
        // given
        Farm testFarm = mock(Farm.class);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(testFarm));

        Region mockRegion = mock(Region.class);
        when(mockRegion.getName()).thenReturn("서울");
        when(testFarm.getRegion()).thenReturn(mockRegion);

        // when
        var result = farmService.findById(1L);

        // then
        assertThat(result).isNotNull();
        verify(farmRepository).findById(1L);
    }

    @Test
    @DisplayName("findById: 농장 ID로 조회한 농장이 존재하지 않으면 예외를 던진다")
    void findById_withNotFoundFarm_shouldThrowFarmNotFoundException() {
        // given
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> farmService.findById(1L))
                .isInstanceOf(FarmNotFoundException.class);
    }

    @Test
    @DisplayName("findAll: 페이징 처리된 농장 목록을 조회하고 응답 DTO를 반환")
    void findAll_withPaging_shouldReturnFarmInfoResponseDTOs() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Farm farm1 = mock(Farm.class);
        Farm farm2 = mock(Farm.class);
        Page<Farm> mockPage = new PageImpl<>(List.of(farm1, farm2));

        when(farmRepository.findAllFarms(pageable)).thenReturn(mockPage);

        Region mockRegion = mock(Region.class);
        when(mockRegion.getName()).thenReturn("서울");
        when(farm1.getRegion()).thenReturn(mockRegion);
        when(farm2.getRegion()).thenReturn(mockRegion);

        // when
        var result = farmService.findAll(pageable);

        // then
        assertThat(result.farmInfoResponseDTOS()).hasSize(2);
    }

    @Test
    @DisplayName("giveBadge: 농장에 뱃지를 부여하고 응답 DTO를 반환")
    void giveBadge_withValidFarmIdAndBadgeName_shouldReturnFarmDetailInfoResponseDTO() {
        // given
        Farm testFarm = mock(Farm.class);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(testFarm));
        when(farmBadgeRepository.findByName("테스트 뱃지")).thenReturn(Optional.of(mock(FarmBadge.class)));

        Region mockRegion = mock(Region.class);
        when(mockRegion.getName()).thenReturn("서울");
        when(testFarm.getRegion()).thenReturn(mockRegion);

        // when
        var result = farmService.giveBadge(1L, "테스트 뱃지");

        // then
        assertThat(result).isNotNull();
        verify(farmRepository).findById(1L);
        verify(testFarm).addFarmBadge(any());
    }

    private FarmSaveRequestDTO createFarmSaveRequestDTO() {
        return new FarmSaveRequestDTO(
                TEST_FARM_NAME,
                TEST_FARM_REPRESENTATIVE,
                TEST_PHONE_NUMBER,
                TEST_BUSINESS_REGISTRATION_NUMBER,
                TEST_ADDRESS
        );

    }

    private FarmBadge createOriginCertificationBadge() {
        try {
            Constructor<FarmBadge> constructor = FarmBadge.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            FarmBadge farmBadge = constructor.newInstance();

            ReflectionTestUtils.setField(farmBadge, "id", 1L);
            ReflectionTestUtils.setField(farmBadge, "name", "원산지 인증");
            ReflectionTestUtils.setField(farmBadge, "img", "origin.png");

            return farmBadge;
        } catch (Exception e){
            throw new RuntimeException("농장 뱃지 생성 실패", e);
        }
    }

    private Region createTestFarmRegion() {
        try{
            Constructor<Region> constructor = Region.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Region region = constructor.newInstance();

            ReflectionTestUtils.setField(region, "id", 1L);
            ReflectionTestUtils.setField(region, "name", "서울");

            return region;
        } catch (Exception e) {
            throw new RuntimeException("농장 지역 생성 실패", e);
        }
    }
}
