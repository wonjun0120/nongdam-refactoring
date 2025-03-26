package shop.nongdam.nongdambackend.ingredient.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.nongdam.nongdambackend.ingredient.domain.Ingredient;

import java.util.List;
import java.util.Optional;

import static shop.nongdam.nongdambackend.ingredient.domain.QIngredient.ingredient;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientCustomRepositoryImpl implements IngredientCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Ingredient> findAllIngredients(Pageable pageable) {
        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(ingredient.count())
                        .from(ingredient)
                        .fetchOne()
        ).orElse(0L);

        List<Ingredient> ingredients = jpaQueryFactory
                .selectFrom(ingredient)
                .orderBy(ingredient.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(ingredients, pageable, total);
    }
}
