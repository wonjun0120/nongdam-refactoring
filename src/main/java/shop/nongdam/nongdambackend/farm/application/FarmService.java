package shop.nongdam.nongdambackend.farm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.auth.exception.EmailNotFoundException;
import shop.nongdam.nongdambackend.farm.api.dto.request.FarmSaveRequestDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmDetailInfoResponseDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmInfoResponseDTO;
import shop.nongdam.nongdambackend.farm.api.dto.response.FarmInfoResponseDTOs;
import shop.nongdam.nongdambackend.farm.domain.Farm;
import shop.nongdam.nongdambackend.farm.domain.FarmBadge;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmBadgeRepository;
import shop.nongdam.nongdambackend.farm.domain.repository.FarmRepository;
import shop.nongdam.nongdambackend.farm.exception.FarmAlreadyExistException;
import shop.nongdam.nongdambackend.farm.exception.FarmNotFoundException;
import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;
import shop.nongdam.nongdambackend.farm.exception.FarmBadgeNotFoundException;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.region.domain.Region;
import shop.nongdam.nongdambackend.region.domain.repository.RegionRepository;
import shop.nongdam.nongdambackend.region.exception.RegionNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmService {
    private final FarmRepository farmRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final FarmBadgeRepository farmBadgeRepository;

    @Transactional
    public FarmInfoResponseDTO saveFarmInfo(String email, FarmSaveRequestDTO farmSaveRequestDTO){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        Optional<Farm> farmOptional = farmRepository.findByMemberId(member.getId());
        if (farmOptional.isPresent()) {
            throw new FarmAlreadyExistException();
        }

        FarmBadge originCertificationBadge = farmBadgeRepository.findByName("원산지 인증")
                .orElseThrow(FarmBadgeNotFoundException::new);

        Farm farm = buildNewFarm(member, farmSaveRequestDTO);
        farm.addFarmBadge(originCertificationBadge);
        farmRepository.save(farm);

        member.updateRole(Role.ROLE_PRODUCER);
        return FarmInfoResponseDTO.from(farm);
    }

    public FarmDetailInfoResponseDTO findById(Long farmId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(FarmNotFoundException::new);

        return FarmDetailInfoResponseDTO.from(farm);
    }

    public FarmInfoResponseDTOs findAll(Pageable pageable) {
        Page<Farm> farms = farmRepository.findAllFarms(pageable);

        List<FarmInfoResponseDTO> farmInfoResponseDTOs = farms.stream()
                .map(FarmInfoResponseDTO::from)
                .toList();

        return FarmInfoResponseDTOs.of(farmInfoResponseDTOs, PageInfoResDto.from(farms));
    }

    public FarmDetailInfoResponseDTO giveBadge(Long farmId, String badgeName) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(FarmNotFoundException::new);

        FarmBadge farmBadge = farmBadgeRepository.findByName(badgeName)
                .orElseThrow(FarmBadgeNotFoundException::new);

        farm.addFarmBadge(farmBadge);
        return FarmDetailInfoResponseDTO.from(farm);
    }

    private Farm buildNewFarm(Member member, FarmSaveRequestDTO farmSaveRequestDTO){
        String address = farmSaveRequestDTO.address();
        Region matchedRegion = null;

        Map<String, List<String>> regionKeywords = Map.of(
                "서울", List.of("서울"),
                "경기/인천", List.of("경기", "인천"),
                "강원", List.of("강원"),
                "대전/세종", List.of("대전", "세종"),
                "충남/충북", List.of("충남", "충북", "충청남도", "충청북도"),
                "전남/전북", List.of("광주", "전남", "전북", "전라남도", "전라북도"),
                "경남/경북", List.of("대구", "경북", "경남", "경상북도", "경상남도"),
                "부산", List.of("부산", "울산"),
                "제주", List.of("제주")
        );

        for (Map.Entry<String, List<String>> entry : regionKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (address.contains(keyword)) {
                    matchedRegion = regionRepository.findByName(entry.getKey())
                            .orElseThrow(RegionNotFoundException::new);
                    break;
                }
            }
            if (matchedRegion != null) {
                break;
            }
        }


        return Farm.builder()
                .member(member)
                .farmName(farmSaveRequestDTO.farmName())
                .farmRepresentative(farmSaveRequestDTO.farmRepresentative())
                .phoneNumber(farmSaveRequestDTO.phoneNumber())
                .businessRegistrationNumber(farmSaveRequestDTO.businessRegistrationNumber())
                .address(farmSaveRequestDTO.address())
                .region(matchedRegion)
                .build();
    }



}
