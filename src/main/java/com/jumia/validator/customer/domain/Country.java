package com.jumia.validator.customer.domain;

/**
 * @author jacksonvieira
 *
 */
public enum Country {

	CAMEROON(237), ETHIOPIA(251), MOROCCO(212), MOZAMBIQUE(258), UGANDA(256);

	public Integer countryCode;

	Country(Integer value) {
		countryCode = value;
	}

	public Integer getCountryCode() {
		return countryCode;
	}
}
