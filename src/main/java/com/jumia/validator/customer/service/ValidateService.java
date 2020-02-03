package com.jumia.validator.customer.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jumia.validator.customer.domain.Country;

@Service
public class ValidateService {

	private Map<Country, ValidatorPhoneNumber> validateMap;

	/**
	 * Constructor
	 */
	public ValidateService() {
		validateMap = new HashMap<>();
		this.populateMap();
	}

	/**
	 * Method responsible for populate de map of ValidatorNumberServices
	 */
	private void populateMap() {
		ValidatorCameroon validatorCameroon = new ValidatorCameroon();
		ValidatorEthiopia validatorEthiopia = new ValidatorEthiopia();
		ValidatorMorocco validatorMorocco = new ValidatorMorocco();
		ValidatorMozambique validatorMozambique = new ValidatorMozambique();
		ValidatorUganda validatorUganda = new ValidatorUganda();

		this.validateMap.put(Country.CAMEROON, validatorCameroon);
		this.validateMap.put(Country.ETHIOPIA, validatorEthiopia);
		this.validateMap.put(Country.MOROCCO, validatorMorocco);
		this.validateMap.put(Country.MOZAMBIQUE, validatorMozambique);
		this.validateMap.put(Country.UGANDA, validatorUganda);
	}

	/**
	 * Get the Instance ValidatorPhoneNumber
	 * 
	 * @param country - Enum Country
	 * @return Instance ValidatorPhoneNumber
	 */
	public ValidatorPhoneNumber getValidator(Country country) {
		return this.validateMap.get(country);
	}
}
