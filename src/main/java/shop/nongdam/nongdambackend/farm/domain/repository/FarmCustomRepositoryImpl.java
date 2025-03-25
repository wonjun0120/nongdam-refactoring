package shop.nongdam.nongdambackend.farm.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.farm.domain.Farm;

import java.util.List;
import java.util.Optional;

import static shop.nongdam.nongdambackend.farm.domain.QFarm.farm;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmCustomRepositoryImpl implements FarmCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Farm> findAllFarms(Pageable pageable) {
        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(farm.count())
                        .from(farm)
                        .fetchOne()
        ).orElse(0L);

        List<Farm> farms = jpaQueryFactory
                .selectFrom(farm)
                .orderBy(farm.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(farms, pageable, total);
    }
}
