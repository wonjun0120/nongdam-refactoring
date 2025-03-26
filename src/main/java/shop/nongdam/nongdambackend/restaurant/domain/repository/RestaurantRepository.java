package shop.nongdam.nongdambackend.restaurant.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantCustomRepository {
}
