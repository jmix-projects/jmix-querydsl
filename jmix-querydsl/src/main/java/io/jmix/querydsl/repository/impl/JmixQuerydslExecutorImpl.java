package io.jmix.querydsl.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlanRepository;
import io.jmix.core.Metadata;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.impl.repository.support.method_metadata.CrudMethodMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;
import io.jmix.querydsl.JmixQuerydslQuery;
import io.jmix.querydsl.repository.JmixQuerydslExecutor;
import io.jmix.querydsl.JmixQuerydslFactory;

import java.io.Serializable;
import java.util.Map;

@NoRepositoryBean
public class JmixQuerydslExecutorImpl<T> implements JmixQuerydslExecutor<T> {

    protected final UnconstrainedDataManager unconstrainedDataManager;
    private final DataManager dataManager;
    private final FetchPlanRepository fetchPlanRepository;

    private final Metadata metadata;
    private final CrudMethodMetadata.Accessor methodMetadataAccessor;
    private final EntityPath<T> path;

    public JmixQuerydslExecutorImpl(Class<T> domainClass,
                                    ApplicationContext ctx,
                                    EntityPathResolver resolver,
                                    CrudMethodMetadata.Accessor methodMetadataAccessor) {
        this.dataManager = ctx.getBean(DataManager.class);
        this.unconstrainedDataManager = dataManager.unconstrained();
        this.metadata = ctx.getBean(Metadata.class);
        this.fetchPlanRepository = ctx.getBean(FetchPlanRepository.class);
        this.methodMetadataAccessor = methodMetadataAccessor;
        this.path = resolver.createPath(domainClass);
    }

    protected UnconstrainedDataManager getDataManager() {
        return methodMetadataAccessor.getCrudMethodMetadata().isApplyConstraints() ? dataManager : unconstrainedDataManager;
    }

    protected Map<String, Serializable> getHints() {
        return methodMetadataAccessor.getCrudMethodMetadata().getQueryHints();
    }

    @Override
    public JmixQuerydslQuery<T> select() {
        return query().from(path).select(path);
    }

    @Override
    public <U> JmixQuerydslQuery<U> select(Expression<U> expr) {
        return query().from(path).select(expr);
    }

    @Override
    public JmixQuerydslQuery<Tuple> select(Expression<?>... exprs) {
        return query().from(path).select(exprs);
    }

    private JmixQuerydslQuery<?> query() {
        JmixQuerydslFactory jmixQueryFactory = new JmixQuerydslFactory(getDataManager(), metadata, fetchPlanRepository);
        JmixQuerydslQuery<?> query = jmixQueryFactory.query();
        getHints().forEach(query::setHint);
        return query;
    }
}
