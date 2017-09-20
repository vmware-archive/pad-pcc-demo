package io.pivotal.service;

import io.pivotal.domain.Customer;
import io.pivotal.repo.jpa.CustomerRepository;

import java.util.List;

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
		
		List<Customer> customers = jpaCustomerRepository.findByEmail(email);
		
		return customers.size() == 0 ? null : customers.get(0);
	}

}
