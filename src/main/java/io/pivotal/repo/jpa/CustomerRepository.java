package io.pivotal.repo.jpa;

import io.pivotal.domain.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JpaCustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, String> {

	List<Customer> findByEmail(final String email);
	
}
