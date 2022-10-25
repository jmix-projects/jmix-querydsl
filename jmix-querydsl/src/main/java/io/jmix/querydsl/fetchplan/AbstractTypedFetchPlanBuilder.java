package io.jmix.querydsl.fetchplan;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.jmix.core.FetchMode;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlanBuilder;
import io.jmix.core.FetchPlans;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class AbstractTypedFetchPlanBuilder<Q extends EntityPath<?>, V extends AbstractTypedFetchPlanBuilder<Q, V>> {

    protected Q fetchPlanType;

    protected FetchPlanBuilder fetchPlanBuilder;

    protected final FetchPlans fetchPlans;

    public AbstractTypedFetchPlanBuilder(FetchPlans fetchPlans) {
        this.fetchPlans = fetchPlans;
    }

    public AbstractTypedFetchPlanBuilder(Q fetchPlanType,
                                         FetchPlanBuilder fetchPlanBuilder,
                                         FetchPlans fetchPlans) {
        this(fetchPlans);
        this.fetchPlanType = fetchPlanType;
        this.fetchPlanBuilder = fetchPlanBuilder;
    }

    /**
     * Initializes Builder with base type
     *
     * @param fetchPlanType base type
     * @return builder
     */
    public V fetchPlan(Q fetchPlanType) {
        this.fetchPlanBuilder = fetchPlanBuilder(fetchPlanType);
        this.fetchPlanType = fetchPlanType;

        return (V) this;
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public V fetchPlan(Q fetchPlanType, Path<?>... properties) {
        List<Path<?>> propertiesList =
                Arrays.stream(properties).collect(toList());

        return this.fetchPlan(fetchPlanType, propertiesList);
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public V fetchPlan(Q fetchPlanType, List<Path<?>> properties) {
        List<String> propertyNames = properties.stream()
                .map(this::resolvePropertyName).collect(toList());

        FetchPlanBuilder localFetchPlanBuilder = fetchPlanBuilder(fetchPlanType);

        propertyNames.forEach(localFetchPlanBuilder::add);

        this.fetchPlanBuilder = localFetchPlanBuilder;
        this.fetchPlanType = fetchPlanType;

        return (V) this;
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param fetchPlanType base type
     * @param properties basic properties
     * @return builder
     */
    public V fetchPlan(Q fetchPlanType, Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(fetchPlanType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        FetchPlanBuilder localFetchPlanBuilder = fetchPlanBuilder(fetchPlanType);

        propertyNames.forEach(localFetchPlanBuilder::add);

        this.fetchPlanType = fetchPlanType;
        this.fetchPlanBuilder = localFetchPlanBuilder;

        return (V) this;
    }

    /**
     * Applies predefined fetch plans
     *
     * @param fetchPlans predefined fetch plans
     * @return builder
     */
    public V extendByFetchPlans(FetchPlan... fetchPlans) {

        FetchPlanBuilder localFetchPlanBuilder = fetchPlanBuilder();
        Arrays.stream(fetchPlans).forEach(localFetchPlanBuilder::addFetchPlan);

        return (V) this;
    }

    /**
     * Applies predefined fetchPlans
     *
     * @param fetchPlans predefined fetchPlans
     * @return builder
     */
    public V extendByFetchPlans(String... fetchPlans) {

        FetchPlanBuilder localFetchPlanBuilder = fetchPlanBuilder();
        Arrays.stream(fetchPlans).forEach(localFetchPlanBuilder::addFetchPlan);

        return (V) this;
    }

    /**
     * Adds system properties
     *
     * @return builder
     */
    public V extendBySystem() {

        fetchPlanBuilder().addSystem();

        return (V) this;
    }

    /**
     * Adds properties
     *
     * @param properties basic properties
     * @return builder
     */
    public V properties(Path<?>... properties) {
        List<String> propertyNames = Arrays.stream(properties)
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(fetchPlanBuilder()::add);

        return (V) this;
    }

    /**
     * Adds properties
     *
     * @param properties basic properties
     * @return builder
     */
    public V properties(Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(fetchPlanType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(fetchPlanBuilder()::add);

        return (V) this;
    }

    /**
     * Add property
     *
     * @param property property
     * @return builder
     */
    public V property(Path<?> property) {
        fetchPlanBuilder().add(resolvePropertyName(property));

        return (V) this;
    }

    // --- generic methods for paths like entity, bean and other variants

    /**
     * Adds property with fetchPlan
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends EntityPath<?>> V property(SP property, FetchPlan fetchPlan) {

        fetchPlanBuilder().add(resolvePropertyName(property), vb -> vb.addFetchPlan(fetchPlan));

        return (V) this;

    }

    /**
     * Adds property with fetchPlan and fetch mode
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends EntityPath<?>> V property(SP property, FetchPlan fetchPlan, FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }

    /**
     * Adds property with fetchPlan
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends EntityPath<?>> V property(SP property, String fetchPlan) {

        fetchPlanBuilder().add(resolvePropertyName(property), fetchPlan);

        return (V) this;
    }

    /**
     * Adds property with fetchPlan and fetch mode
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param fetchMode fetch mode
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends EntityPath<?>>
    V property(SP property, String fetchPlan, FetchMode fetchMode) {

        fetchPlanBuilder().add(resolvePropertyName(property), fetchPlan, fetchMode);

        return (V) this;
    }

    /**
     * Adds property with sub-properties
     *
     * @param property property
     * @param properties sub-properties
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends EntityPath<?>>
    V property(SP property, Function<SP, List<Path<?>>> properties) {

        List<Path<?>> evaluatedProperties = properties.apply(property);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        fetchPlanBuilder().add(resolvePropertyName(property),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    /**
     * Adds property with sub-properties and fetch mode
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends EntityPath<?>> V property(SP property, Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }


    // --- collection methods

    /**
     * Adds collection property with fetchPlan
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>> V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               FetchPlan fetchPlan) {

        fetchPlanBuilder().add(resolvePropertyName(property), vb -> vb.addFetchPlan(fetchPlan));

        return (V) this;
    }

    /**
     * Adds collection property with fetchPlan
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>> V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               FetchPlan fetchPlan, FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }

    /**
     * Adds collection property with fetchPlan
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String fetchPlan) {

        fetchPlanBuilder().add(resolvePropertyName(property), fetchPlan);

        return (V) this;
    }

    /**
     * Adds collection property with fetchPlan and fetch mode
     *
     * @param property property
     * @param fetchPlan fetchPlan
     * @param fetchMode fetch mode
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String fetchPlan, FetchMode fetchMode) {

        fetchPlanBuilder().add(resolvePropertyName(property), fetchPlan, fetchMode);

        return (V) this;
    }


    /**
     * Adds collection property with sub-properties
     *
     * @param property property
     * @param properties sub-properties
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               Function<SP, List<Path<?>>> properties) {


        List<Path<?>> evaluatedProperties = properties.apply(property.any());
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        fetchPlanBuilder().add(resolvePropertyName(property),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    /**
     * Adds collection property with sub-properties and fetch mode
     *
     * Jmix doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>> V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("Jmix doesn't support such API");
    }


    /**
     * Allows configuring part of builder with native Jmix API
     *
     * @param builder Jmix fetch plan builder
     * @return builder
     */
    public V withFetchPLanBuilder(Consumer<FetchPlanBuilder> builder) {
        builder.accept(fetchPlanBuilder());
        return (V) this;
    }

    // --- build

    /**
     * Builds Jmix fetch plan
     *
     * @return Jmix fetch plan
     */
    public FetchPlan build() {
        return fetchPlanBuilder().build();
    }

    // --- utils methods

    protected FetchPlanBuilder fetchPlanBuilder(Q entityPath) {
        if (fetchPlanBuilder == null) {
            return fetchPlans.builder(entityPath.getType());
        }

        return fetchPlanBuilder;
    }

    protected FetchPlanBuilder fetchPlanBuilder() {
        if (fetchPlanBuilder == null) {
            throw new IllegalArgumentException("Builder isn't initialized");
        }

        return this.fetchPlanBuilder;
    }

    protected String resolvePropertyName(Path<?> property) {
        return property.getMetadata().getName();
    }
}
