package io.pivotal.repo.jpa;

import io.pivotal.domain.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JpaCustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, String> {

	Customer findByEmail(final String email);
	
}
