package io.pivotal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import io.pivotal.domain.Customer;

import javax.sql.DataSource;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

@Configuration
@EnableCaching
@Profile("local")
public class DemoLocalConfig {
	
	@Autowired 
	GemFireCache clientCache;
	
	@Bean
	public DataSource dataSource() {

		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase dataSource = builder
			.setType(EmbeddedDatabaseType.HSQL)
			.addScript("sql/create_table.sql")
			.build();
		
		return dataSource;
	}
	
	@Bean(name = "gemfireCache")
	public ClientCacheFactoryBean clientCache() {
		ClientCacheFactoryBean ccf = new ClientCacheFactoryBean();
		ccf.addLocators(new ConnectionEndpoint("localhost", 10334));
		ccf.setPdxSerializer(new ReflectionBasedAutoSerializer(".*"));
		ccf.setPdxReadSerialized(false);
		ccf.setSubscriptionEnabled(true);

		return ccf;
	}
	
	@Bean(name = "customer")
	public ClientRegionFactoryBean<String, Customer> customerRegion() {
		ClientRegionFactoryBean<String, Customer> customerRegionFactory = new ClientRegionFactoryBean<>();
		customerRegionFactory.setCache(clientCache);
		customerRegionFactory.setShortcut(ClientRegionShortcut.PROXY);
		customerRegionFactory.setName("customer");

		return customerRegionFactory;
	}
	
	@Bean(name="cacheManager")
	public GemfireCacheManager createGemfireCacheManager() {

		GemfireCacheManager gemfireCacheManager = new GemfireCacheManager();
		gemfireCacheManager.setCache(clientCache);

		return gemfireCacheManager;
	}
}
