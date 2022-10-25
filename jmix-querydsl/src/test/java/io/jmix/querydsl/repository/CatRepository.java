package io.jmix.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import io.jmix.core.repository.ApplyConstraints;
import io.jmix.core.repository.JmixDataRepository;
import io.jmix.querydsl.JmixQuerydslQuery;
import io.jmix.querydsl.JmixQuerydslExecutor;
import io.jmix.querydsl.domain.Cat;

import java.util.List;
import java.util.UUID;

public interface CatRepository extends JmixDataRepository<Cat, UUID>,
        JmixQuerydslExecutor<Cat>,
        ExCatRepository {
    List<Cat> findAllByName(String name);

    @ApplyConstraints(value = false)
    @Override
    JmixQuerydslQuery<Cat> select();

    @ApplyConstraints(value = false)
    @Override
    <U> JmixQuerydslQuery<U> select(Expression<U> expr);

    @ApplyConstraints(value = false)
    @Override
    JmixQuerydslQuery<Tuple> select(Expression<?>... exprs);
}
