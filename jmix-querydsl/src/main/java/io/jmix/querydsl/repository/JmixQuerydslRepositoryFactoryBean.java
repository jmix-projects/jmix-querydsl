package io.jmix.querydsl.repository;

import io.jmix.core.impl.repository.support.JmixRepositoryFactory;
import io.jmix.core.impl.repository.support.JmixRepositoryFactoryBean;
import io.jmix.core.impl.repository.support.method_metadata.CrudMethodMetadataAccessingPostProcessor;
import io.jmix.querydsl.repository.impl.JmixQuerydslExecutorImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

public class JmixQuerydslRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends JmixRepositoryFactoryBean<T, S, ID> {
    private ApplicationContext ctx;

    public JmixQuerydslRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new JmixQuerydslRepositoryFactory(ctx);
    }

    public static final class JmixQuerydslRepositoryFactory extends JmixRepositoryFactory {
        private ApplicationContext ctx;
        private final CrudMethodMetadataAccessingPostProcessor crudMethodPostProcessor;

        public JmixQuerydslRepositoryFactory(ApplicationContext ctx) {
            super(ctx);
            this.ctx = ctx;
            this.crudMethodPostProcessor = new CrudMethodMetadataAccessingPostProcessor();
            addRepositoryProxyPostProcessor(crudMethodPostProcessor);
        }

        @Override
        protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
            boolean isQueryDslRepository = QUERY_DSL_PRESENT
                    && JmixQuerydslExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());

            if (isQueryDslRepository) {
                return RepositoryComposition.RepositoryFragments.just(new JmixQuerydslExecutorImpl<>(
                        metadata.getDomainType(),
                        ctx,
                        SimpleEntityPathResolver.INSTANCE,
                        crudMethodPostProcessor));
            }

            return super.getRepositoryFragments(metadata);
        }
    }
}
