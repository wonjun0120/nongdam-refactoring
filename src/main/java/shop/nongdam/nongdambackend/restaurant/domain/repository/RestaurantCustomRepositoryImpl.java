package shop.nongdam.nongdambackend.restaurant.domain.repository;

import static shop.nongdam.nongdambackend.restaurant.domain.QRestaurant.restaurant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.restaurant.domain.Restaurant;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantCustomRepositoryImpl implements RestaurantCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Restaurant> findAllRestaurants(Pageable pageable) {
        long total = Optional.ofNullable(
                queryFactory
                        .select(restaurant.count())
                        .from(restaurant)
                        .fetchOne()
        ).orElse(0L);

        List<Restaurant> restaurants = queryFactory
                .selectFrom(restaurant)
                .orderBy(restaurant.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(restaurants, pageable, total);
    }

    @Override
    public Page<Restaurant> findRegisteredRestaurants(Pageable pageable) {
        long total = Optional.ofNullable(
                queryFactory
                        .select(restaurant.count())
                        .from(restaurant)
                        .where(restaurant.isRegistered.eq(true))
                        .fetchOne()
        ).orElse(0L);

        List<Restaurant> registeredRestaurants = queryFactory
                .selectFrom(restaurant)
                .where(restaurant.isRegistered.eq(true))
                .orderBy(restaurant.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(registeredRestaurants, pageable, total);
    }
}
