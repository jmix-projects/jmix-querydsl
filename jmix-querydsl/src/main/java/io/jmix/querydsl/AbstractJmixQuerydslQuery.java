package io.jmix.querydsl;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.jpa.*;
import io.jmix.core.*;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.core.metamodel.model.MetaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for Jmix API based implementations of the JmixQuery interface
 *
 * Based on {@link com.querydsl.jpa.impl.AbstractJPAQuery}
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 */
public abstract class AbstractJmixQuerydslQuery<T, Q extends AbstractJmixQuerydslQuery<T, Q>> extends JmixQuerydslQueryBase<T, Q> {

    private static final long serialVersionUID = 5397497620388267860L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractJmixQuerydslQuery.class);

    protected final Multimap<String, Serializable> hints = LinkedHashMultimap.create();

    protected final UnconstrainedDataManager dataManager;

    protected final Metadata metadata;

    protected final FetchPlanRepository fetchPlanRepository;

    protected final QueryHandler queryHandler;


    public AbstractJmixQuerydslQuery(DataManager dataManager, Metadata metadata, FetchPlanRepository fetchPlanRepository) {
        this(dataManager, metadata, fetchPlanRepository, EclipseLinkTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public AbstractJmixQuerydslQuery(UnconstrainedDataManager dataManager,
                                     Metadata metadata,
                                     FetchPlanRepository fetchPlanRepository,
                                     JPQLTemplates templates,
                                     QueryMetadata queryMetadata) {
        super(queryMetadata, templates);
        this.queryHandler = templates.getQueryHandler();
        this.dataManager = dataManager;
        this.metadata = metadata;
        this.fetchPlanRepository = fetchPlanRepository;
    }

    @Override
    public long fetchCount() {
        try {
            return internalFetchCount();
        } finally {
            reset();
        }
    }

    protected long internalFetchCount() {
        return new CountQueryFlow().applyFlow().one();
    }

    @SuppressWarnings("unchecked")
    protected <CT> Class<CT> getQueryResultType() {
        Expression<?> projection = getMetadata().getProjection();
        Class<?> projectionType = projection == null ? Object.class : projection.getType();
        return (Class<CT>) projectionType;
    }

    @Override
    public CloseableIterator<T> iterate() {
        Iterator<T> iterator = fetch().iterator();
        FactoryExpression<?> projection = getFactoryExpressionProjection();
        if (projection != null) {
            return new TransformingIterator<T>(iterator, projection);
        } else {
            return new IteratorAdapter<T>(iterator);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
         return internalFetch(FetchPlan.LOCAL);
    }

    public List<T> fetch(FetchPlan fetchPlan) {
         return internalFetch(fetchPlan);
    }

    public List<T> fetch(String fetchPlan) {
         return internalFetch(fetchPlan);
    }

    private List<T> internalFetch(Object fetchPlan) {

        try {
            return internalFetchImplementation(FetchPlanParam.of(fetchPlan));
        } finally {
            reset();
        }
    }

    private List<T> internalFetchImplementation(FetchPlanParam fetchPlanParam) {
        Class<?> queryResultType = getQueryResultType();

        FactoryExpression<?> projection = getFactoryExpressionProjection();
        if (projection != null) {
            List<String> propertyNames = getProjectionPropertyNames(projection.getArgs());
            List<KeyValueEntity> loadValues = new KeyValuesQueryFlow(propertyNames)
                    .modifiers(getMetadata().getModifiers())
                    .applyFlow().list();

            List<Object> rv = new ArrayList<>(loadValues.size());
            for (KeyValueEntity keyValueEntity : loadValues) {
                if (keyValueEntity != null) {
                    rv.add(projection.newInstance(convertKeyValueToObject(keyValueEntity, propertyNames)));
                } else {
                    rv.add(projection.newInstance(new Object[]{null}));
                }
            }
            return (List<T>) rv;
        }

        if (Entity.class.isAssignableFrom(queryResultType)) {
            LoadContext<Entity> loadContext = new EntityQueryFlow<>()
                    .modifiers(getMetadata().getModifiers())
                    .fetchPlan(fetchPlanParam)
                    .applyFlow();

            return (List<T>) dataManager.loadList(loadContext);
        } else {
            return new OneColumnQueryFlow<T>().modifiers(getMetadata().getModifiers()).applyFlow().list();
        }
    }

    private static List<String> getProjectionPropertyNames(List<Expression<?>> projection) {
        List<Expression<?>> args = projection;
        List<String> propertyNames = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            propertyNames.add(Integer.toString(i));
        }
        return propertyNames;
    }

    private FactoryExpression<?> getFactoryExpressionProjection() {
        Expression<?> tmpPrj = getMetadata().getProjection();
        return tmpPrj instanceof FactoryExpression<?> ? (FactoryExpression<?>) tmpPrj : null;
    }

    private Object[] convertKeyValueToObject(KeyValueEntity keyValueEntity,
                                             List<String> properties) {
        Object[] result = new Object[properties.size()];
        for (int i = 0, propertiesSize = properties.size(); i < propertiesSize; i++) {
            Object value = keyValueEntity.getValue(properties.get(i));
            result[i] = value;
        }
        return result;
    }

    @Override
    public QueryResults<T> fetchResults() {
        return internalFetchResults(FetchPlan.LOCAL);
    }

    public QueryResults<T> fetchResults(FetchPlan fetchPlan) {
        return internalFetchResults(fetchPlan);
    }

    public QueryResults<T> fetchResults(String fetchPlan) {
       return internalFetchResults(fetchPlan);
    }

    public QueryResults<T> internalFetchResults(Object fetchPlan) {
        try {
            return internalFetchResultImplementation(FetchPlanParam.of(fetchPlan));
        } finally {
            reset();
        }

    }

    private QueryResults<T> internalFetchResultImplementation(FetchPlanParam fetchPlanParam) {

        long total = internalFetchCount();

        if (total > 0) {
            List<T> list = internalFetch(fetchPlanParam);

            return new QueryResults<>(list,
                    getMetadata().getModifiers(), total);

        } else {
            return QueryResults.emptyResults();
        }
    }

    protected void logQuery(String queryString, Map<Object, String> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            MDC.put(MDC_QUERY, normalizedQuery);
            MDC.put(MDC_PARAMETERS, String.valueOf(parameters));
            logger.debug(normalizedQuery);
        }
    }

    protected void cleanupMDC() {
        MDC.remove(MDC_QUERY);
        MDC.remove(MDC_PARAMETERS);
    }

    @Override
    protected void reset() {
        cleanupMDC();
    }

    @Nullable
    @Override
    public T fetchOne() {
        return internalFetchOne(FetchPlan.LOCAL);
    }

    @Nullable
    public T fetchOne(String fetchPlan) {
        return internalFetchOne(fetchPlan);
    }

    @Nullable
    public T fetchOne(FetchPlan fetchPlan) {
        return internalFetchOne(fetchPlan);
    }

    @Nullable
    public T internalFetchOne(Object fetchPlan) {
        try {

            return internalFetchOneImplementation(FetchPlanParam.of(fetchPlan));

        } catch (javax.persistence.NoResultException e) {
            logger.trace(e.getMessage(),e);
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        } finally {
            reset();
        }

    }

    @Nullable
    @SuppressWarnings("unchecked")
    private T internalFetchOneImplementation(FetchPlanParam fetchPlanParam) {
        Class<?> queryResultType = getQueryResultType();

        FactoryExpression<?> projection = getFactoryExpressionProjection();
        if (projection != null) {
            List<String> propertyNames = getProjectionPropertyNames(projection.getArgs());
            KeyValueEntity keyValueEntity = new KeyValuesQueryFlow(propertyNames)
                    .modifiers(getMetadata().getModifiers())
                    .applyFlow().one();
            return (T) projection.newInstance(convertKeyValueToObject(keyValueEntity, propertyNames));
        }

        if (Entity.class.isAssignableFrom(queryResultType)) {
            LoadContext<Entity> loadContext = new EntityQueryFlow<>()
                    .modifiers(getMetadata().getModifiers())
                    .fetchPlan(fetchPlanParam)
                    .applyFlow();

            return (T) dataManager.load(loadContext);
        } else {
            return new OneColumnQueryFlow<T>().applyFlow()
                    .optional().orElse(null);
        }
    }

    @SuppressWarnings("unchecked")
    public Q setHint(String name, Serializable value) {
        hints.put(name, value);
        return (Q) this;
    }

    @Override
    protected JPQLSerializer createSerializer() {
        return new JmixQuerydslJpqlSerializer(getTemplates());
    }

    protected void clone(Q query) {
        hints.putAll(query.hints);
    }

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     *
     * @param transactionalDataManager entity manager
     * @return cloned query
     */
    public abstract Q clone(DataManager transactionalDataManager);

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     * and the specified templates
     *
     * @param transactionalDataManager entity manager
     * @param templates templates
     * @return cloned query
     */
    public abstract Q clone(UnconstrainedDataManager transactionalDataManager, JPQLTemplates templates);

    /**
     * Clone the state of this query to a new instance
     *
     * @return cloned query
     */
    @Override
    public Q clone() {
        return clone(dataManager, getTemplates());
    }

    public interface CommonQueryFlow<Q, F extends CommonQueryFlow<Q, F>> {

        Q createQuery();

        Q applyParameters(Q query);

        Q applyModifiers(Q query);

        Q applyHints(Q query);

        default Q applyFlow() {
            Q q = createQuery();
            Q qWithParams = applyParameters(q);
            Q qWithParamsAndModifiers = applyModifiers(qWithParams);
            return applyHints(qWithParamsAndModifiers);
        }
    }

    public class KeyValuesQueryFlow
            implements CommonQueryFlow<FluentValuesLoader, KeyValuesQueryFlow> {

        protected QueryModifiers queryModifiers = QueryModifiers.EMPTY;
        protected JPQLSerializer serializer;
        private List<String> properties;

        public KeyValuesQueryFlow(List<String> properties) {
            this.properties = properties;
        }

        public KeyValuesQueryFlow modifiers(QueryModifiers modifiers) {
            this.queryModifiers = modifiers;

            return this;
        }

        @Override
        public FluentValuesLoader createQuery() {
            serializer = serialize(false);
            String queryString = serializer.toString();


            return dataManager.loadValues(queryString).properties(properties);
        }

        @Override
        public FluentValuesLoader applyParameters(FluentValuesLoader query) {

            Map<Object, String> constants = serializer.getConstantToLabel();
            Map<ParamExpression<?>, Object> params = getMetadata().getParams();

            for (Map.Entry<Object, String> entry : constants.entrySet()) {

                String key = entry.getValue();

                Object val = resolveValByParams(entry.getKey(), params);

                query.parameter(JmixQuerydslJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
            }

            return query;
        }

        @Override
        public FluentValuesLoader applyModifiers(FluentValuesLoader query) {
            if (queryModifiers != null && queryModifiers.isRestricting()) {
                Integer limit = queryModifiers.getLimitAsInteger();
                Integer offset = queryModifiers.getOffsetAsInteger();
                if (limit != null) {
                    query.maxResults(limit);
                }
                if (offset != null) {
                    query.firstResult(offset);
                }
            }

            return query;
        }

        @Override
        public FluentValuesLoader applyHints(FluentValuesLoader query) {
            return query;
        }
    }

    public abstract class AbstractFluentValueLoaderQueryFlow<FT, F extends AbstractFluentValueLoaderQueryFlow<FT, F>>
            implements CommonQueryFlow<FluentValueLoader<FT>, F> {

        protected QueryModifiers queryModifiers = QueryModifiers.EMPTY;
        protected JPQLSerializer serializer;

        public F modifiers(QueryModifiers modifiers) {
            this.queryModifiers = modifiers;

            return (F) this;
        }

        @Override
        public FluentValueLoader<FT> createQuery() {
            serializer = serialize(false);
            String queryString = serializer.toString();

            return dataManager.loadValue(queryString, getQueryResultType());
        }

        @Override
        public FluentValueLoader<FT> applyParameters(FluentValueLoader<FT> query) {

            Map<Object, String> constants = serializer.getConstantToLabel();
            Map<ParamExpression<?>, Object> params = getMetadata().getParams();

            for (Map.Entry<Object, String> entry : constants.entrySet()) {

                String key = entry.getValue();

                Object val = resolveValByParams(entry.getKey(), params);

                query.parameter(JmixQuerydslJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
            }

            return query;
        }

        @Override
        public FluentValueLoader<FT> applyModifiers(FluentValueLoader<FT> query) {
            if (queryModifiers != null && queryModifiers.isRestricting()) {
                Integer limit = queryModifiers.getLimitAsInteger();
                Integer offset = queryModifiers.getOffsetAsInteger();
                if (limit != null) {
                    query.maxResults(limit);
                }
                if (offset != null) {
                    query.firstResult(offset);
                }
            }

            return query;
        }

        @Override
        public FluentValueLoader<FT> applyHints(FluentValueLoader<FT> query) {
            return query;
        }
    }

    public class CountQueryFlow extends AbstractFluentValueLoaderQueryFlow<Long, CountQueryFlow> {

        @Override
        public FluentValueLoader<Long> createQuery() {
            serializer = serialize(true);
            String queryString = serializer.toString();

            return dataManager.loadValue(queryString, Long.class);
        }


        @Override
        public FluentValueLoader<Long> applyModifiers(FluentValueLoader<Long> query) {
            return query;
        }

        @Override
        public FluentValueLoader<Long> applyHints(FluentValueLoader<Long> query) {
            return query;
        }
    }

    public class OneColumnQueryFlow<FT> extends AbstractFluentValueLoaderQueryFlow<FT, OneColumnQueryFlow<FT>> {}

    public abstract class AbstractLoadContextQueryFlow<E extends Entity, F extends AbstractLoadContextQueryFlow<E, F>>
            implements CommonQueryFlow<LoadContext<E>, F> {

        protected QueryModifiers queryModifiers = QueryModifiers.EMPTY;
        protected FetchPlanParam fetchPlanParam;

        protected JPQLSerializer serializer;

        public F modifiers(QueryModifiers modifiers) {
            this.queryModifiers = modifiers;

            return (F) this;
        }

        @Override
        public LoadContext<E> createQuery() {
            serializer = serialize(false);
            String queryString = serializer.toString();

            Class<Object> queryResultType = getQueryResultType();
            MetaClass metadataClass = metadata.getClass(queryResultType);
            LoadContext<E> loadContext = new LoadContext<>(metadataClass);
            loadContext.setQueryString(queryString);

            // apply fetch plan
            if (fetchPlanParam != null) {
                FetchPlan fetchPlan = fetchPlanParam.getFetchPlan();
                String fetchPlanName = fetchPlanParam.getFetchPlanName();
                if (fetchPlan != null) {
                    // if both fetch plans are filled in FetchPlanParam than object FetchPlan will set
                    loadContext.setFetchPlan(fetchPlan);
                } else if (fetchPlanName != null) {
                    fetchPlan = fetchPlanRepository.getFetchPlan(metadataClass, fetchPlanName);
                    loadContext.setFetchPlan(fetchPlan);
                }
            }

            return loadContext;
        }

        @Override
        public LoadContext<E> applyParameters(LoadContext<E> loadContext) {

            Map<Object, String> constants = serializer.getConstantToLabel();
            Map<ParamExpression<?>, Object> params = getMetadata().getParams();

            LoadContext.Query query = loadContext.getQuery();

            for (Map.Entry<Object, String> entry : constants.entrySet()) {

                String key = entry.getValue();

                Object val = resolveValByParams(entry.getKey(), params);

                query.setParameter(JmixQuerydslJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
            }

            return loadContext;
        }

        @Override
        public LoadContext<E> applyModifiers(LoadContext<E> loadContext) {

            LoadContext.Query query = loadContext.getQuery();
            if (queryModifiers != null && queryModifiers.isRestricting()) {
                Integer limit = queryModifiers.getLimitAsInteger();
                Integer offset = queryModifiers.getOffsetAsInteger();
                if (limit != null) {
                    query.setMaxResults(limit);
                }
                if (offset != null) {
                    query.setFirstResult(offset);
                }
            }

            return loadContext;
        }

        @Override
        public LoadContext<E> applyHints(LoadContext<E> loadContext) {

            for (Map.Entry<String, Serializable> entry : hints.entries()) {
                loadContext.setHint(entry.getKey(), entry.getValue());
            }

            return loadContext;
        }

        public F fetchPlan(FetchPlanParam fetchPlanParam) {
            this.fetchPlanParam = fetchPlanParam;

            return (F) this;
        }
    }


    public class EntityQueryFlow<E extends Entity>
            extends AbstractLoadContextQueryFlow<E, EntityQueryFlow<E>> {

    }


    private Object resolveValByParams(Object val, Map<ParamExpression<?>, Object> params) {

        Object resolved = val;
        if (val instanceof Param) {
            resolved = params.get(val);
            if (resolved == null) {
                throw new ParamNotSetException((Param<?>) val);
            }
        }

        return resolved;
    }

    static class FetchPlanParam {

        protected String fetchPlanName;

        protected FetchPlan fetchPlan;

        public FetchPlanParam(String fetchPlanName) {
            this.fetchPlanName = fetchPlanName;
        }

        public FetchPlanParam(FetchPlan fetchPlan) {
            this.fetchPlan = fetchPlan;
        }

        public static FetchPlanParam of(Object fetchPlan) {
            if (fetchPlan instanceof AbstractJmixQuerydslQuery.FetchPlanParam) {
                return (FetchPlanParam) fetchPlan;
            }

            if (fetchPlan instanceof String) {
                return new FetchPlanParam((String) fetchPlan);
            }

            if (fetchPlan instanceof FetchPlan) {
                return new FetchPlanParam((FetchPlan) fetchPlan);
            }

            throw new IllegalArgumentException("Library supports only fetch plan as String or io.jmix.core.FetchPlan");
        }

        @Nullable
        public String getFetchPlanName() {
            return fetchPlanName;
        }

        @Nullable
        public FetchPlan getFetchPlan() {
            return fetchPlan;
        }
    }
}
