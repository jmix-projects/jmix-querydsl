package io.jmix.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

public interface JmixQuerydslExecutor<T> {

    JmixQuerydslQuery<T> select();

    <U> JmixQuerydslQuery<U> select(Expression<U> expr);

    JmixQuerydslQuery<Tuple> select(Expression<?>... exprs);
}
