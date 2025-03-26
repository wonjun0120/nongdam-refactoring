package shop.nongdam.nongdambackend.restaurant.appllication;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.aws.application.ImageService;
import shop.nongdam.nongdambackend.global.dto.PageInfoResDto;
import shop.nongdam.nongdambackend.member.domain.Member;
import shop.nongdam.nongdambackend.member.domain.Role;
import shop.nongdam.nongdambackend.member.domain.repository.MemberRepository;
import shop.nongdam.nongdambackend.member.exception.MemberNotFoundException;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantDetailSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.request.RestaurantSaveRequestDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantDetailInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantInfoResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantTagResponseDTO;
import shop.nongdam.nongdambackend.restaurant.api.dto.response.RestaurantTagResponseDTOs;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;
import shop.nongdam.nongdambackend.restaurant.domain.repository.RestaurantRepository;
import shop.nongdam.nongdambackend.restaurant.exception.AccessDeniedRestaurantException;
import shop.nongdam.nongdambackend.restaurant.exception.RestaurantNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    @Transactional
    public RestaurantInfoResponseDTO save(String memberEmail, RestaurantSaveRequestDTO restaurantSaveRequestDTO) {
        Member member = getMemberByEmail(memberEmail);

        Restaurant restaurant = buildNewRestaurant(restaurantSaveRequestDTO, member);
        restaurantRepository.save(restaurant);

        updateMemberRoleToRestaurant(member);

        return RestaurantInfoResponseDTO.from(restaurant);
    }

    @Transactional
    public RestaurantDetailInfoResponseDTO registerRestaurantDetail(
            String email,
            RestaurantDetailSaveRequestDTO restaurantDetailSaveRequestDTO,
            MultipartFile restaurantImage) {

        Member member = getMemberByEmail(email);
        Restaurant restaurant = getRestaurantById(restaurantDetailSaveRequestDTO.restaurantId());

        validateRestaurantOwnership(member, restaurant);

        updateRestaurantDetails(restaurant, restaurantDetailSaveRequestDTO, restaurantImage);

        return RestaurantDetailInfoResponseDTO.from(restaurant);
    }

    public RestaurantDetailInfoResponseDTO findById(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        return RestaurantDetailInfoResponseDTO.from(restaurant);
    }

    public RestaurantTagResponseDTOs findAll(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findRegisteredRestaurants(pageable);

        List<RestaurantTagResponseDTO> restaurantTagResponseDTOs = restaurants.stream()
                .map(RestaurantTagResponseDTO::from)
                .toList();

        return RestaurantTagResponseDTOs.of(restaurantTagResponseDTOs, PageInfoResDto.from(restaurants));
    }

    @Transactional
    public void deleteById(String email, Long restaurantId) {
        Member member = getMemberByEmail(email);
        Restaurant restaurant = getRestaurantById(restaurantId);

        validateRestaurantOwnership(member, restaurant);
        restaurant.delete();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
    }

    private void validateRestaurantOwnership(Member member, Restaurant restaurant) {
        if (!restaurant.isOwner(member)) {
            throw new AccessDeniedRestaurantException();
        }
    }

    private void updateMemberRoleToRestaurant(Member member) {
        member.updateRole(Role.ROLE_RESTAURANT);
    }

    private void updateRestaurantDetails(
            Restaurant restaurant,
            RestaurantDetailSaveRequestDTO restaurantDetailSaveRequestDTO,
            MultipartFile restaurantImage) {

        restaurant.updateDetail(
                restaurantDetailSaveRequestDTO.latitude(),
                restaurantDetailSaveRequestDTO.longitude(),
                imageService.saveImage(restaurantImage),
                restaurantDetailSaveRequestDTO.openTime(),
                restaurantDetailSaveRequestDTO.closeTime(),
                restaurantDetailSaveRequestDTO.precautions(),
                true);
    }

    private Restaurant buildNewRestaurant(RestaurantSaveRequestDTO restaurantSaveRequestDTO, Member member) {
        return Restaurant.builder()
                .restaurantName(restaurantSaveRequestDTO.restaurantName())
                .restaurantRepresentative(restaurantSaveRequestDTO.restaurantRepresentative())
                .member(member)
                .phoneNumber(restaurantSaveRequestDTO.phoneNumber())
                .businessRegistrationNumber(restaurantSaveRequestDTO.businessRegistrationNumber())
                .address(restaurantSaveRequestDTO.address())
                .build();
    }
}
