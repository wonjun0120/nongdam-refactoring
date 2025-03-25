package shop.nongdam.nongdambackend.farm.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.nongdam.nongdambackend.farm.domain.Farm;

import java.util.Optional;

public interface FarmRepository extends JpaRepository<Farm, Long>, FarmCustomRepository {
    Optional<Farm> findByMemberId(Long memberId);
}
