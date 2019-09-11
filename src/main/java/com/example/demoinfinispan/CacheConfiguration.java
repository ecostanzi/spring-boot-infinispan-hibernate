package com.example.demoinfinispan;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.starter.embedded.InfinispanCacheConfigurer;
import org.infinispan.spring.starter.embedded.InfinispanGlobalConfigurer;
import org.infinispan.transaction.TransactionMode;
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
    public InfinispanGlobalConfigurer globalConfiguration() {
        return () -> GlobalConfigurationBuilder
                .defaultClusteredBuilder().defaultCacheName("infinispan-demo-cluster-cache")
                .transport().defaultTransport()
                .addProperty("configurationFile", "jgroups-config.xml")
                .clusterName("infinispan-demo-cluster").globalJmxStatistics()
                .enabled(true)
                .allowDuplicateDomains(true)
                .build();
    }

    @Bean
    public InfinispanCacheConfigurer cacheConfigurer() {

        return manager -> {
            // initialize application cache
            manager.defineConfiguration("spring-cache", new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(2)
                    .jmxStatistics().enabled(false).eviction()
                    .type(EvictionType.COUNT).size(1000).expiration()
                    .lifespan(1, TimeUnit.MINUTES).build());


            // initialize Hibernate L2 cache
            manager.defineConfiguration("entity", new ConfigurationBuilder().clustering().cacheMode(CacheMode.INVALIDATION_SYNC)
                    .jmxStatistics().enabled(true)
                    .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("replicated-entity", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_SYNC)
                    .jmxStatistics().enabled(true)
                    .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("local-query", new ConfigurationBuilder().clustering().cacheMode(CacheMode.LOCAL)
                    .jmxStatistics().enabled(true)
                    .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("replicated-query", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_ASYNC)
                    .jmxStatistics().enabled(true)
                    .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("timestamps", new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_ASYNC)
                    .jmxStatistics().enabled(true)
                    .locking().concurrencyLevel(1000).lockAcquisitionTimeout(15000).build());
            manager.defineConfiguration("pending-puts", new ConfigurationBuilder().clustering().cacheMode(CacheMode.LOCAL)
                    .jmxStatistics().enabled(true)
                    .simpleCache(true).transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL).expiration().maxIdle(60000).build());

            setCacheManager(manager);
        };
    }

}
