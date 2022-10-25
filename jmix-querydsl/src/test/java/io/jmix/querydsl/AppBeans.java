package io.jmix.querydsl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

public class AppBeans {
    private static ApplicationContext applicationContext;

    private static void setApplicationContext(ApplicationContext applicationContext) {
        AppBeans.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T get(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    @Component
    static class ApplicationContextRetriever {
        @EventListener
        public void storeApplicationContext(ContextRefreshedEvent event) {
            AppBeans.setApplicationContext(event.getApplicationContext());
        }
    }
}
