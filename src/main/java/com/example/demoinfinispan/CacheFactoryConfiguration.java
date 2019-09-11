package com.example.demoinfinispan;

import org.hibernate.service.ServiceRegistry;
import org.infinispan.hibernate.cache.v53.InfinispanRegionFactory;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.Properties;

public class CacheFactoryConfiguration extends InfinispanRegionFactory {

    private static final long serialVersionUID = 1L;

    /**
     * Defines an Hibernate L2 cache: hibernate-infinispan
     */
    @Override
    protected EmbeddedCacheManager createCacheManager(Properties properties, ServiceRegistry serviceRegistry) {
        // Not a managed bean from the Spring Context (as it gets created through reflection) and hence override the static instance
        return CacheConfiguration.getCacheManager();
    }



}
