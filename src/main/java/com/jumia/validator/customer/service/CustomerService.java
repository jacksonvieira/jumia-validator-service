package com.jumia.validator.customer.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jumia.validator.customer.domain.Country;
import com.jumia.validator.customer.domain.Customer;
import com.jumia.validator.customer.repository.CustomerRepository;
import com.jumia.validator.lib.service.JumiaException;
import com.jumia.validator.lib.service.JumiaService;
import com.jumia.validator.lib.validation.JumiaViolationException;

/**
 * @author jacksonvieira
 *
 */
@Service
@Transactional(rollbackFor = { JumiaException.class })
public class CustomerService extends JumiaService<Customer> {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private ValidateService validateService;

	@Cacheable("customers")
	public Page<Customer> all(Pageable pageable) {
		return repository.findAllPageable(pageable);
	}

	public Page<Customer> all(List<Pair<String, List<String>>> filters, Pageable pageable) {

		return repository.findAllPageable(pageable);
	}

	@Cacheable("customers")
	public List<Customer> all() {

		return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));

	}

	@Override
	public Customer find(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Customer save(Customer t) throws JumiaException {
		Customer savedCustomer = null;
		try {
			savedCustomer = repository.saveAndFlush(t);
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new JumiaViolationException(e.getMessage());
			}
		}
		return savedCustomer;
	}

	/**
	 * That method it is override from parent class
	 * 
	 * @param id - The customer id
	 */
	@Override
	public void delete(Long id) {
		repository.deleteById(id);

	}

	/**
	 * Find by Phone number
	 * 
	 * @param phone - The number of phone
	 * @return Instance Customer
	 */
	public Customer findByPhone(String phone) {
		return repository.findByPhone(phone);
	}

	/**
	 * Find Customer by name
	 * 
	 * @param name - The name of Customer
	 * @return Instance Customer
	 */
	public Customer findByName(String name) {
		return repository.findByName(name);
	}

	/**
	 * Create a Customer
	 * 
	 * @param t - Customer instance
	 * @return Instance Customer saved
	 * @throws JumiaException
	 */
	public Customer create(Customer t) throws JumiaException {
		Customer savedCustomer = null;
		try {
			savedCustomer = this.save(t);
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new JumiaViolationException(e.getMessage());
			}
		}
		return savedCustomer;

	}

	/**
	 * Extract the code Country with a regular expression The format param is (code)
	 * + number, ex: (258) 84330678235
	 * 
	 * @param phone - The full String number
	 * @return Instance Integer
	 */
	public Integer tryToExtractCountry(String phone) {
		Integer countryCode = null;
		Pattern p = Pattern.compile("\\(.*?\\)");
		Matcher m = p.matcher(phone);
		if (m.find())
			countryCode = Integer.parseInt(m.group().subSequence(1, m.group().length() - 1).toString());

		return countryCode;
	}

	public String tryToExtractOnlyPhoneNumber(String phone) {
		return StringUtils.substringAfterLast(phone, ")");
	}

	/**
	 * This method it is responsible for validating the phone number, There is a
	 * service called ValidateService, in that service, there is a map with some
	 * objects implementations from interface ValidatorPhoneNumber
	 * 
	 * @param country - Enum
	 * @param phone   - The phone number
	 * @return
	 */
	public Boolean validatePhone(Country country, String phone) {
		ValidatorPhoneNumber validate = validateService.getValidator(country);
		return validate.validate(phone);
	}

}
