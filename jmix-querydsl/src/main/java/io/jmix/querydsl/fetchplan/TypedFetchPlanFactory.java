package io.jmix.querydsl.fetchplan;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import io.jmix.core.FetchPlans;

import java.util.List;
import java.util.function.Function;

public class TypedFetchPlanFactory {
    private final FetchPlans fetchPlans;

    public TypedFetchPlanFactory(FetchPlans fetchPlans) {
        this.fetchPlans = fetchPlans;
    }

    /**
     * Initializes Builder with base type
     *
     * @param fetchPlanType base type
     * @return builder
     */
    public <Q extends EntityPath<?>>
    TypedFetchPlanBuilder<Q> fetchPlan(Q fetchPlanType) {
        return this.<Q>builder().fetchPlan(fetchPlanType);
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public <Q extends EntityPath<?>> TypedFetchPlanBuilder<Q> fetchPlan(Q fetchPlanType, Path<?>... properties) {
        return this.<Q>builder().fetchPlan(fetchPlanType, properties);
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public <Q extends EntityPath<?>>
    TypedFetchPlanBuilder<Q> fetchPlan(Q fetchPlanType, List<Path<?>> properties) {
        return this.<Q>builder().fetchPlan(fetchPlanType, properties);
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public <Q extends EntityPath<?>>
    TypedFetchPlanBuilder<Q> fetchPlan(Q fetchPlanType, Function<Q, List<Path<?>>> properties) {
        return this.<Q>builder().fetchPlan(fetchPlanType, properties);
    }

    protected  <Q extends EntityPath<?>> TypedFetchPlanBuilder<Q> builder() {
        return new TypedFetchPlanBuilder<>(fetchPlans);
    }
}
