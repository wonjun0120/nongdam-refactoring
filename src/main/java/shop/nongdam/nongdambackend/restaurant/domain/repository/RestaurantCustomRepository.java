package shop.nongdam.nongdambackend.restaurant.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

public interface RestaurantCustomRepository {
    Page<Restaurant> findAllRestaurants(Pageable pageable);
    Page<Restaurant> findRegisteredRestaurants(Pageable pageable);

}
