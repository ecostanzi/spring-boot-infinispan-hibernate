package com.example.demoinfinispan;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
@CacheConfig(cacheNames = "spring-cache")
class MyDummyService {

    private final EntityManager em;

    private final ProjectRepository projectRepository;

    private final CacheManager cacheManager;

    public MyDummyService(EntityManager em, ProjectRepository projectRepository, CacheManager cacheManager) {
        this.em = em;
        this.projectRepository = projectRepository;
        this.cacheManager = cacheManager;
    }

    public void save(Long id) {
        if(!projectRepository.existsById(id)){
            Project project = new Project();
            project.setId(id);
            project.setName("Project" + id);
            em.persist(project);
        }
    }

    @Cacheable
    public Project projectNameCached(Long id) {
        return this.em.find(Project.class, id);
    }

    public Project projectNameNoCache(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

}
