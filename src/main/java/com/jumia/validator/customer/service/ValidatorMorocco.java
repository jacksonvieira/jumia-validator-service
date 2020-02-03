package com.jumia.validator.customer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacksonvieira
 *
 */
public class ValidatorMorocco implements ValidatorPhoneNumber {

	private static String regex = "\\(212\\)\\ ?[5-9]\\d{8}$";

	@Override
	public Boolean validate(String phoneNumber) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.find();
	}

}
