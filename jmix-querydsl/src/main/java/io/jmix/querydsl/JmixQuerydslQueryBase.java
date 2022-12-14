package io.jmix.querydsl;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.FetchableSubQueryBase;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;

/**
 * {@code JmixQueryBase} is a base Query class for JPA queries
 *
 * Based on {@link com.querydsl.jpa.JPAQueryBase}
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 */
public abstract class JmixQuerydslQueryBase<T, Q extends JmixQuerydslQueryBase<T, Q>> extends FetchableSubQueryBase<T, Q> implements JPQLQuery<T> {

    private static final long serialVersionUID = 8062566124236044336L;

    protected final JPAQueryMixin<Q> queryMixin;

    private final JPQLTemplates templates;

    @SuppressWarnings("unchecked")
    public JmixQuerydslQueryBase(QueryMetadata md, JPQLTemplates templates) {
        super(new JPAQueryMixin<Q>(md));
        super.queryMixin.setSelf((Q) this);
        this.queryMixin = (JPAQueryMixin<Q>) super.queryMixin;
        this.templates = templates;
    }

    protected JPQLTemplates getTemplates() {
        return templates;
    }

    protected abstract JPQLSerializer createSerializer();

    protected JPQLSerializer serialize(boolean forCountRow) {
        return serialize(forCountRow, true);
    }

    protected JPQLSerializer serialize(boolean forCountRow, boolean validate) {
        if (validate) {
            if (queryMixin.getMetadata().getJoins().isEmpty()) {
                throw new IllegalArgumentException("No sources given");
            }
        }
        JPQLSerializer serializer = createSerializer();
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);

        return serializer;
    }

    protected abstract void reset();

    @Override
    public Q fetchJoin() {
        return queryMixin.fetchJoin();
    }

    @Override
    public Q fetchAll() {
        return queryMixin.fetchAll();
    }

    public Q from(EntityPath<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> Q from(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.from(Expressions.as((Path<P>) target, alias));
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q innerJoin(MapExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q join(CollectionExpression<?,P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q join(EntityPath<P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(EntityPath<P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q join(MapExpression<?,P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q leftJoin(CollectionExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q leftJoin(EntityPath<P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q leftJoin(MapExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(CollectionExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(EntityPath<P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(MapExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }


    @Override
    public String toString() {
        JPQLSerializer serializer = serialize(false, false);
        return serializer.toString().trim();
    }

    @Override
    public abstract Q clone();
}
