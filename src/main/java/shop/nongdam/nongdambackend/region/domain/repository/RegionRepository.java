package shop.nongdam.nongdambackend.region.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.region.domain.Region;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByName(String region);
}
