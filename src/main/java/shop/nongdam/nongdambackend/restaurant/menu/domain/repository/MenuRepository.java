package shop.nongdam.nongdambackend.restaurant.menu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.restaurant.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
