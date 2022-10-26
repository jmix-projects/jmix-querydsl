package io.jmix.querydsl;

import io.jmix.core.annotation.JmixModule;
import io.jmix.core.repository.EnableJmixDataRepositories;
import io.jmix.querydsl.repository.JmixQuerydslRepositoryFactoryBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(JmixQuerydslConfiguration.class)
@JmixModule(id = "io.jmix.querydsl.test", dependsOn = JmixQuerydslConfiguration.class)
@EnableJmixDataRepositories(value = "io.jmix.querydsl",
        repositoryFactoryBeanClass = JmixQuerydslRepositoryFactoryBean.class)
@PropertySource(name = "io.jmix.querydsl", value = "classpath:/io/jmix/querydsl/module.properties")
public class JmixQuerydslTestConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }
}
