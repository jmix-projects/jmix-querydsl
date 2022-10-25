package io.jmix.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.InsertClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.JPQLTemplates;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlanRepository;
import io.jmix.core.Metadata;
import io.jmix.core.UnconstrainedDataManager;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Factory class for query and DML clause creation
 *
 * Based on {@link com.querydsl.jpa.impl.JPAQueryFactory}
 */
public class JmixQuerydslFactory implements JPQLQueryFactory {
    @Nullable
    private final JPQLTemplates templates;

    private final Supplier<UnconstrainedDataManager> dataManagerProvider;

    private final Supplier<Metadata> metadataProvider;

    private final Supplier<FetchPlanRepository> fetchPlanRepositoryProvider;

    public JmixQuerydslFactory(UnconstrainedDataManager dataManager,
                               Metadata metadata,
                               FetchPlanRepository fetchPlanRepository) {
        this(null, () -> dataManager, () -> metadata, () -> fetchPlanRepository);
    }

    public JmixQuerydslFactory(@Nullable JPQLTemplates templates,
                               DataManager dataManager,
                               Metadata metadata,
                               FetchPlanRepository fetchPlanRepository) {
        this(templates, () -> dataManager, () -> metadata, () -> fetchPlanRepository);
    }

    public JmixQuerydslFactory(Supplier<UnconstrainedDataManager> dataManagerProvider,
                               Supplier<Metadata> metadataProvider,
                               Supplier<FetchPlanRepository> fetchPlanRepositoryProvider) {
        this(null, dataManagerProvider, metadataProvider, fetchPlanRepositoryProvider);
    }

    public JmixQuerydslFactory(@Nullable JPQLTemplates templates,
                               Supplier<UnconstrainedDataManager> dataManagerProvider,
                               Supplier<Metadata> metadataProvider,
                               Supplier<FetchPlanRepository> fetchPlanRepositoryProvider) {
        this.dataManagerProvider = dataManagerProvider;
        this.templates = templates;
        this.metadataProvider = metadataProvider;
        this.fetchPlanRepositoryProvider = fetchPlanRepositoryProvider;
    }

    @Override
    public <T> JmixQuerydslQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public JmixQuerydslQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> JmixQuerydslQuery<T> selectDistinct(Expression<T> expr) {
        return select(expr).distinct();
    }

    @Override
    public JmixQuerydslQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return select(exprs).distinct();
    }

    @Override
    public JmixQuerydslQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public JmixQuerydslQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public <T> JmixQuerydslQuery<T> selectFrom(EntityPath<T> from) {
        return select(from).from(from);
    }

    @Override
    public JmixQuerydslQuery<?> from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public JmixQuerydslQuery<?> from(EntityPath<?>... from) {
        return query().from(from);
    }

    @Override
    public UpdateClause<?> update(EntityPath<?> path) {
        throw new UnsupportedOperationException("Jmix QueryDsl doesn't support update queries");
    }

    @Override
    public InsertClause<?> insert(EntityPath<?> entityPath) {
        throw new UnsupportedOperationException("Jmix QueryDsl doesn't support insert queries");
    }

    @Override
    public DeleteClause<?> delete(EntityPath<?> path) {
        throw new UnsupportedOperationException("Jmix QueryDsl doesn't support delete queries");
    }

    @Override
    public JmixQuerydslQuery<?> query() {
        if (templates != null) {
            return new JmixQuerydslQuery<Void>(dataManagerProvider.get(), metadataProvider.get(), fetchPlanRepositoryProvider.get(), templates);
        } else {
            return new JmixQuerydslQuery<Void>(dataManagerProvider.get(), metadataProvider.get(), fetchPlanRepositoryProvider.get());
        }
    }
}
