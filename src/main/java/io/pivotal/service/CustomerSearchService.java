package io.pivotal.service;

import io.pivotal.domain.Customer;
import io.pivotal.repo.jpa.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class CustomerSearchService {
	
	@Autowired
	CustomerRepository jpaCustomerRepository;
	
	private volatile boolean cacheMiss = false;
	
	public boolean isCacheMiss() {
		boolean isCacheMiss = this.cacheMiss;
		this.cacheMiss = false;
		return isCacheMiss;
	}

	protected void setCacheMiss() {
		this.cacheMiss = true;
	}
	
	@Cacheable(value = "customer")
	public Customer getCustomerByEmail(String email) {
		
		setCacheMiss();

		Customer customer = jpaCustomerRepository.findByEmail(email);

		return customer;
	}

}
