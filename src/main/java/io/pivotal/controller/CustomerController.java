package io.pivotal.controller;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.pivotal.domain.Customer;
import io.pivotal.service.CustomerSearchService;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
	
	@Autowired
	io.pivotal.repo.pcc.CustomerRepository pccCustomerRepository;
	
	@Autowired
	io.pivotal.repo.jpa.CustomerRepository jpaCustomerRepository;
	
	@Resource(name = "customer")
	Region<String, Customer> customerRegion;
	
	@Autowired
	CustomerSearchService customerSearchService;
	
	Fairy fairy = Fairy.create();
	

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
		customerRegion.removeAll(customerRegion.keySetOnServer());
		return "Region cleared";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/showdb")
	@ResponseBody
	public String showDB() throws Exception {
		StringBuilder result = new StringBuilder();
		Pageable topTen = new PageRequest(0, 10);
		
		jpaCustomerRepository.findAll(topTen).forEach(item->result.append(item+"<br/>"));
		
		return "Top 10 customers are shown here: <br/>" + result.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/loaddb")
	@ResponseBody
	public String loadDB(@RequestParam(value = "amount", required = true) String amount) throws Exception {
		
		List<Customer> customers = new ArrayList<>();
		
		Integer num = Integer.parseInt(amount);
		
		for (int i=0; i<num; i++) {
			Person person = fairy.person();
			Customer customer = new Customer(person.passportNumber(), person.fullName(), person.email(), person.getAddress().toString(), person.dateOfBirth().toString());
			customers.add(customer);
		}
		
		jpaCustomerRepository.saveAll(customers);
		
		return "New customers successfully saved into Database";
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
	
	@RequestMapping(method = RequestMethod.GET, path = "/countdb")
	@ResponseBody
	public Long countDB() throws Exception {
		return jpaCustomerRepository.count();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/countcache")
	@ResponseBody
	public Long countCache() throws Exception {
		return pccCustomerRepository.count();
	}
	
}
