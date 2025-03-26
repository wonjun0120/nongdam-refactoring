package shop.nongdam.nongdambackend.farm.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.farm.domain.FarmBadge;

import java.util.Optional;

public interface FarmBadgeRepository extends JpaRepository<FarmBadge, Long> {
    Optional<FarmBadge> findByName(String name);
}
