package com.jumia.validator.customer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacksonvieira
 *
 */
public class ValidatorUganda implements ValidatorPhoneNumber {
	private static String regex = "\\(256\\)\\ ?\\d{9}$";

	@Override
	public Boolean validate(String phoneNumber) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.find();
	}
}
