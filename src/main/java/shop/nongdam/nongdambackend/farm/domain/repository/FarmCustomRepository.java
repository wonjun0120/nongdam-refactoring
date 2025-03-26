package shop.nongdam.nongdambackend.farm.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.nongdam.nongdambackend.farm.domain.Farm;


public interface FarmCustomRepository {
    Page<Farm> findAllFarms(Pageable pageable);
}
