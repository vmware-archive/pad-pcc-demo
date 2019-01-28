package io.pivotal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableLogging;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.geode.config.annotation.EnableDurableClient;
import org.springframework.geode.config.annotation.UseMemberName;

/**
 * Created by nchandrappa on 6/12/18.
 */
@Configuration
@EnableDurableClient(id = "cache-demo")
@EnableEntityDefinedRegions(basePackages = {"io.pivotal.domain"})
@EnableLogging(logLevel = "info")
@UseMemberName("PivotalCloudCacheDemoApplication")
@EnableJpaRepositories(basePackages = {"io.pivotal.repo.jpa"})
public class CloudCacheConfig {
}
