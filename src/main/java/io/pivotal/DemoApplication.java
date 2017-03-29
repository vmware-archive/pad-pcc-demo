package io.pivotal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableGemfireRepositories(basePackageClasses = io.pivotal.repo.pcc.CustomerRepository.class)
@EnableJpaRepositories(basePackageClasses = io.pivotal.repo.jpa.CustomerRepository.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
