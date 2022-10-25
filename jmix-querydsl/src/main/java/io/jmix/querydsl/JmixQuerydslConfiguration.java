package io.jmix.querydsl;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlanRepository;
import io.jmix.core.FetchPlans;
import io.jmix.core.Metadata;
import io.jmix.core.annotation.JmixModule;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.querydsl.fetchplan.TypedFetchPlanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {EclipselinkConfiguration.class})
@PropertySource(name = "io.jmix.querydsl", value = "classpath:/io/jmix/querydsl/module.properties")
public class JmixQuerydslConfiguration {
    public static final String TYPED_FETCH_PLAN_FACTORY_BEAN_NAME = "qd_TypedFetchPlanFactory";
    public static final String JMIX_QUERYDSL_FACTORY_BEAN_NAME = "qd_JmixQuerydslFactory";


    @Bean(TYPED_FETCH_PLAN_FACTORY_BEAN_NAME)
    public TypedFetchPlanFactory typedFetchPlanFactory(FetchPlans fetchPlans) {
        return new TypedFetchPlanFactory(fetchPlans);
    }

    @Bean(JMIX_QUERYDSL_FACTORY_BEAN_NAME)
    public JmixQuerydslFactory jmixQuerydslFactory(DataManager dataManager,
                                                   Metadata metadata,
                                                   FetchPlanRepository fetchPlanRepository) {
        return new JmixQuerydslFactory(dataManager, metadata, fetchPlanRepository);
    }
}
