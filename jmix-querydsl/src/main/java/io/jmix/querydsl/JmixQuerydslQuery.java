package io.jmix.querydsl;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.EclipseLinkTemplates;
import com.querydsl.jpa.JPQLTemplates;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlanRepository;
import io.jmix.core.Metadata;
import io.jmix.core.UnconstrainedDataManager;

/**
 * Jmix specific JPA query interface.  Provides the functionality defined in
 * {@link javax.persistence.Query} and adds access to the underlying database query for Jmix specific
 * functionality.
 *
 * Based on {@link org.eclipse.persistence.jpa.JpaQuery}
 */
public class JmixQuerydslQuery<T> extends AbstractJmixQuerydslQuerydslQuery<T, JmixQuerydslQuery<T>> {

    private static final long serialVersionUID = - 9180183730489110259L;

    /**
     * Creates a new EntityManager bound query
     *
     * @param dm entity manager
     */
    public JmixQuerydslQuery(UnconstrainedDataManager dm, Metadata metadata, FetchPlanRepository fetchPlanRepository) {
        super(dm, metadata, fetchPlanRepository, EclipseLinkTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param dm entity manager
     * @param queryMetadata query queryMetadata
     */
    public JmixQuerydslQuery(UnconstrainedDataManager dm, Metadata metadata, FetchPlanRepository fetchPlanRepository, QueryMetadata queryMetadata) {
        super(dm, metadata, fetchPlanRepository, EclipseLinkTemplates.DEFAULT, queryMetadata);
    }

    /**
     * Creates a new query
     *
     * @param dm entity manager
     * @param templates templates
     */
    public JmixQuerydslQuery(UnconstrainedDataManager dm, Metadata metadata, FetchPlanRepository fetchPlanRepository, JPQLTemplates templates) {
        super(dm, metadata, fetchPlanRepository, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new query
     *
     * @param dm entity manager
     * @param templates templates
     * @param queryMetadata query queryMetadata
     */
    public JmixQuerydslQuery(UnconstrainedDataManager dm, Metadata metadata, FetchPlanRepository fetchPlanRepository, JPQLTemplates templates, QueryMetadata queryMetadata) {
        super(dm, metadata, fetchPlanRepository, templates, queryMetadata);
    }

    @Override
    public JmixQuerydslQuery<T> clone(UnconstrainedDataManager dataManager, JPQLTemplates templates) {
        JmixQuerydslQuery<T> q = new JmixQuerydslQuery<>(dataManager, metadata, fetchPlanRepository, templates, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public JmixQuerydslQuery<T> clone(DataManager dataManager) {
        return clone(dataManager, EclipseLinkTemplates.DEFAULT);
    }

    @Override
    public <U> JmixQuerydslQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
        JmixQuerydslQuery<U> newType = (JmixQuerydslQuery<U>) this;
        return newType;
    }

    @Override
    public JmixQuerydslQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
        JmixQuerydslQuery<Tuple> newType = (JmixQuerydslQuery<Tuple>) this;
        return newType;
    }

}
