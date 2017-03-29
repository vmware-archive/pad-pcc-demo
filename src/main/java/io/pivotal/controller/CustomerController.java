package io.pivotal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.pivotal.domain.Customer;
import io.pivotal.service.CustomerSearchService;

@RestController
public class CustomerController {
	
	@Autowired
	io.pivotal.repo.pcc.CustomerRepository pccCustomerRepository;
	
	@Autowired
	io.pivotal.repo.jpa.CustomerRepository jpaCustomerRepository; 
	
	@Autowired
	CustomerSearchService customerSearchService;
	
	Fairy fairy = Fairy.create();
	
	
	@RequestMapping("/")
	public String home() {
		return "Customer Search Service -- Available APIs: <br/>"
				+ "<br/>"
				+ "GET /showcache    	               - get all customer info in PCC<br/>"
				+ "GET /clearcache                     - remove all customer info in PCC<br/>"
				+ "GET /showdb  	                   - get all customer info in MySQL<br/>"
				+ "GET /cleardb                        - remove all customer info in MySQL<br/>"
				+ "GET /loaddb                         - load 500 customer info into MySQL<br/>"
				+ "GET /customerSearch?email={email}   - get specific customer info<br/>";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/showcache")
	@ResponseBody
	public String show() throws Exception {
		StringBuilder result = new StringBuilder();
		
		pccCustomerRepository.findAll().forEach(item->result.append(item+"<br/>"));

		return result.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/clearcache")
	@ResponseBody
	public String clearCache() throws Exception {
		pccCustomerRepository.deleteAll();
		return "Region cleared";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/showdb")
	@ResponseBody
	public String showDB() throws Exception {
		StringBuilder result = new StringBuilder();
		
		jpaCustomerRepository.findAll().forEach(item->result.append(item+"<br/>"));
		
		return result.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/loaddb")
	@ResponseBody
	public String loadDB() throws Exception {
		
		List<Customer> customers = new ArrayList<>();
		
		for (int i=0; i<500; i++) {
			Person person = fairy.person();
			Customer customer = new Customer(person.passportNumber(), person.fullName(), person.email(), person.getAddress().toString(), person.dateOfBirth().toString());
			customers.add(customer);
		}
		
		jpaCustomerRepository.save(customers);
		
		return "New 500 customers successfully saved into Database";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/cleardb")
	@ResponseBody
	public String clearDB() throws Exception {
		
		jpaCustomerRepository.deleteAll();
		
		return "Database cleared";
	}
	
	@RequestMapping(value = "/customerSearch", method = RequestMethod.GET)
	public String searchCustomerByEmail(@RequestParam(value = "email", required = true) String email) {
		
		long startTime = System.currentTimeMillis();
		Customer customer = customerSearchService.getCustomerByEmail(email);
		long elapsedTime = System.currentTimeMillis();
		Boolean isCacheMiss = customerSearchService.isCacheMiss();
		String sourceFrom = isCacheMiss ? "MySQL" : "PCC";

		return String.format("Result [<b>%1$s</b>] <br/>"
				+ "Cache Miss [<b>%2$s</b>] <br/>"
				+ "Read from [<b>%3$s</b>] <br/>"
				+ "Elapsed Time [<b>%4$s ms</b>]%n", customer, isCacheMiss, sourceFrom, (elapsedTime - startTime));
	}
	
}
