package io.jmix.querydsl.fetchplan;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.jmix.core.FetchMode;
import io.jmix.core.FetchPlanBuilder;
import io.jmix.core.FetchPlans;

import java.util.Collection;
import java.util.function.BiConsumer;

public class TypedFetchPlanBuilder<Q extends EntityPath<?>> extends AbstractTypedFetchPlanBuilder<Q, TypedFetchPlanBuilder<Q>> {

    public TypedFetchPlanBuilder(FetchPlans fetchPlans) {
        super(fetchPlans);
    }

    public TypedFetchPlanBuilder(Q fetchPlanType, FetchPlanBuilder fetchPlanBuilder, FetchPlans fetchPlans) {
        super(fetchPlanType, fetchPlanBuilder, fetchPlans);
    }

    /**
     * Adds property with typed sub-builder
     *
     * @param property property
     * @param builder sub-builder
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends EntityPath<?>> TypedFetchPlanBuilder<Q> property(SP property, BiConsumer<SP, TypedFetchPlanBuilder<SP>> builder) {

        fetchPlanBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property, new TypedFetchPlanBuilder<>(property, vb, fetchPlans)));

        return this;
    }

    /**
     * Adds property with typed sub-builder and fetch mode
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends EntityPath<?>> TypedFetchPlanBuilder<Q> property(SP property, BiConsumer<SP, TypedFetchPlanBuilder<SP>> builder,
                                      FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }


    /**
     * Adds collection property with typed sub-builder
     *
     * @param property property
     * @param builder sub-builder
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends EntityPathBase<E> & Path<E>>
    TypedFetchPlanBuilder<Q> property(CollectionPathBase<? extends Collection<E>, E, SP> property,
                                      BiConsumer<SP, TypedFetchPlanBuilder<SP>> builder) {

        fetchPlanBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property.any(), new TypedFetchPlanBuilder<>(property.any(), vb, fetchPlans)));

        return this;
    }

    /**
     * Adds collection property with typed sub-builder and fetch mode
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends EntityPathBase<E> & Path<E>>
    TypedFetchPlanBuilder<Q> property(CollectionPathBase<? extends Collection<E>, E, SP> property,
                                      BiConsumer<SP, TypedFetchPlanBuilder<SP>> builder, FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }
}
