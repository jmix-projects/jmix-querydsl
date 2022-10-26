package io.jmix.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import io.jmix.querydsl.JmixQuerydslQuery;

public interface JmixQuerydslExecutor<T> {

    JmixQuerydslQuery<T> select();

    <U> JmixQuerydslQuery<U> select(Expression<U> expr);

    JmixQuerydslQuery<Tuple> select(Expression<?>... exprs);
}
