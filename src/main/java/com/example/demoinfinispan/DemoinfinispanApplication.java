package com.example.demoinfinispan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@SpringBootApplication
public class DemoinfinispanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoinfinispanApplication.class, args);
    }

    @RestController
    public class MyController {

    	@Autowired
        MyDummyService myDummyService;

        @GetMapping("/project/{id}")
        public Project getProject(@PathVariable("id") Long projectId) {
            myDummyService.save(projectId);
            myDummyService.projectNameCached(projectId);
            return myDummyService.projectNameNoCache(projectId);
        }
    }

}
