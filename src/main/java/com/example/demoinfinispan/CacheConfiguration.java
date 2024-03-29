package com.example.demoinfinispan;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    // Initialize the cache in a non Spring-managed bean
    private static EmbeddedCacheManager cacheManager;

    public static EmbeddedCacheManager getCacheManager(){
        return cacheManager;
    }

    public static void setCacheManager(EmbeddedCacheManager cacheManager) {
        CacheConfiguration.cacheManager = cacheManager;
    }

    @Bean
    public InfinispanCacheConfigurer cacheConfigurer() {

        return manager -> {
            // initialize application cache
            manager.defineConfiguration("spring-cache", new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(2)
                    .jmxStatistics().enabled(false).available(false).eviction()
                    .type(EvictionType.COUNT).size(1000).expiration()
                    .lifespan(1, TimeUnit.MINUTES).build());

            setCacheManager(manager);
        };
    }

}
