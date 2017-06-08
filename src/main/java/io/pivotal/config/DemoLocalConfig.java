package io.pivotal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import io.pivotal.domain.Customer;

import javax.sql.DataSource;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

@Configuration
@EnableCaching
@Profile("local")
public class DemoLocalConfig {
	
	@Autowired 
	ClientCache clientCache;

//	@Bean
//    public DataSource dataSource() {
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//             
//            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//            dataSource.setUsername("admin");
//            dataSource.setPassword("admin");
//            dataSource.setUrl("jdbc:mysql://localhost:3306/test");
//            
//            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//            databasePopulator.addScript(new ClassPathResource("sql/create_table.sql"));
//            
//            DatabasePopulatorUtils.execute(databasePopulator, dataSource);
//             
//            return dataSource;
//    }
	
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
	public ClientCache clientCache() {
		ClientCacheFactory ccf = new ClientCacheFactory();
		ccf.addPoolLocator("localhost", 10334);
		ccf.setPdxSerializer(new ReflectionBasedAutoSerializer(".*"));
		ccf.setPdxReadSerialized(false);
		ccf.setPoolSubscriptionEnabled(true);

		ClientCache clientCache = ccf.create();

		return clientCache;
	}
	
	@Bean(name = "customer")
	public Region<String, Customer> customerRegion() {
		ClientRegionFactory<String, Customer> customerRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		Region<String, Customer> customerRegion = customerRegionFactory.create("customer");

		return customerRegion;
	}
	
	@Bean(name="cacheManager")
	public GemfireCacheManager createGemfireCacheManager() {

		GemfireCacheManager gemfireCacheManager = new GemfireCacheManager();
		gemfireCacheManager.setCache(clientCache);

		return gemfireCacheManager;
	}
}
