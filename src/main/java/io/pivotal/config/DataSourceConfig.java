//package io.pivotal.config;
//
//import org.springframework.cloud.config.java.AbstractCloudConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.sql.DataSource;
//
///**
// * Created by nchandrappa on 6/12/18.
// */
//@Configuration
//@SuppressWarnings("unused")
//public class DataSourceConfig extends AbstractCloudConfig {
//
//    @Bean
//	public DataSource dataSource() {
//		DataSource dataSource = connectionFactory().dataSource();
//
//		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//        databasePopulator.addScript(new ClassPathResource("sql/create_table.sql"));
//
//        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
//
//        return dataSource;
//	}
//}
