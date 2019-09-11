package com.example.demoinfinispan;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.embedded.provider.SpringEmbeddedCacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CustomSpringCacheManager extends SpringEmbeddedCacheManager {
    /**
     * @param nativeCacheManager Underlying cache manager
     */
    public CustomSpringCacheManager(EmbeddedCacheManager nativeCacheManager) {
        super(nativeCacheManager);
    }

    @Override
    public Collection<String> getCacheNames() {
        return getNativeCacheManager().getCacheNames()
                .stream()
                .filter(name -> !Arrays.asList("entity",
                        "replicated-entity",
                        "local-query",
                        "replicated-query",
                        "timestamps",
                        "pending-puts").contains(name))
                .collect(Collectors.toList());

    }
}
