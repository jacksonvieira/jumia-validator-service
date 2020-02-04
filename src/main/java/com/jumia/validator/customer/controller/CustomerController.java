package com.jumia.validator.customer.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jumia.validator.customer.domain.Country;
import com.jumia.validator.customer.domain.Customer;
import com.jumia.validator.customer.service.CustomerService;
import com.jumia.validator.lib.properties.JumiaProperties;
import com.jumia.validator.lib.service.JumiaException;

/**
 * @author jacksonvieira
 *
 */
@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

	@Autowired
	private CustomerService service;

	@Autowired
	private JumiaProperties jumiaProperties;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomers(
			@RequestParam(name = "filterBy", required = false) List<String> filterBy) {

		List<Customer> customers = service.all(getFilters(filterBy));

		if (null == customers)
			return new ResponseEntity<List<Customer>>((List<Customer>) null, HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long customerId) throws JumiaException {
		return getCustomer(customerId);
	}

	private ResponseEntity<Customer> getCustomer(Long customerId) throws JumiaException {
		Customer customer = service.find(customerId);
		if (null == customer)
			return new ResponseEntity<Customer>((Customer) null, HttpStatus.NO_CONTENT); // 204

		service.validate(customer);

		return new ResponseEntity<Customer>(customer, HttpStatus.OK); // 200
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer) throws JumiaException {

		customer = service.create(customer);

		HttpHeaders responseHeaders = new HttpHeaders();
		URI uri = null;
		try {
			uri = new URI(jumiaProperties.getUrl() + "/customers/" + customer.getId());
		} catch (URISyntaxException e) {
		}
		if (null != uri)
			responseHeaders.setLocation(uri);

		return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED); // 201
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> editCustomerWithPut(@PathVariable("id") Long customerId, @RequestBody Customer customer)
			throws JumiaException {
		return editWithPut(customerId, customer);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> editCustomerWithPatch(@PathVariable("id") long customerId, @RequestBody Customer customer)
			throws JumiaException {
		return editWithPatch(customerId, customer);
	}

	private ResponseEntity<?> editWithPut(Long customerId, Customer customer) throws JumiaException {
		Customer customerInDatabase = service.find(customerId);
		if (null == customerInDatabase)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204

		service.validate(customerInDatabase);

		customer = service.save(customer);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK); // 200
	}

	private ResponseEntity<Customer> editWithPatch(long customerId, Customer customer) throws JumiaException {
		Customer customerInDatabase = service.find(customerId);
		if (null == customerInDatabase)
			return new ResponseEntity<Customer>((Customer) null, HttpStatus.NO_CONTENT); // 204

		service.validate(customerInDatabase);

		if (null != customer.getName())
			customerInDatabase.setName(customer.getName());
		if (null != customer.getPhone())
			customerInDatabase.setPhone(customer.getPhone());

		customer = service.save(customerInDatabase);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") long customerId) throws JumiaException {
		Customer customer = service.find(customerId);
		if (null == customer)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204

		service.validate(customer);

		service.delete(customerId);
		return new ResponseEntity<>(HttpStatus.OK); // 200
	}

	@RequestMapping(value = "/pages", method = RequestMethod.GET)
	public ResponseEntity<Page<Customer>> getPages(Pageable pageable) {
		Page<Customer> customers = service.all(pageable);
		return new ResponseEntity<Page<Customer>>(customers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/validate", method = RequestMethod.GET)
	public ResponseEntity<?> isValidNumber(@PathVariable("id") Long customerId) throws JumiaException {

		Customer customer = service.find(customerId);
		if (null == customer)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204

		service.validate(customer);
		Boolean isValid = checkIfNumberIsValidFromCountry(customer);
		return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);

	}

	private Boolean checkIfNumberIsValidFromCountry(Customer customer) {
		Boolean isValid = false;
		Integer countryCode = service.tryToExtractCountry(customer.getPhone());
		if (null != countryCode) {
			for (Country country : Country.values()) {
				if (country.getCountryCode().equals(countryCode)) {
					isValid = service.validatePhone(country, customer.getPhone());
					break;
				}
			}
			return isValid;
		}
		return isValid;
	}

	private List<Pair<String, List<String>>> getFilters(List<String> filters) {
		List<Pair<String, List<String>>> filtersList = new ArrayList<>();
		if (filters != null) {
			filters.forEach(filter -> {
				if (filter.trim().isEmpty())
					return;
				String[] filterKeyValue = filter.split(":");
				if (filterKeyValue == null || filterKeyValue.length < 2)
					return;
				String[] filtersValues = filterKeyValue[1].split(";");
				List<String> valuesList = Arrays.asList(filtersValues);
				filtersList.add(Pair.of(filterKeyValue[0], valuesList));
			});
		}
		return filtersList;
	}
}
