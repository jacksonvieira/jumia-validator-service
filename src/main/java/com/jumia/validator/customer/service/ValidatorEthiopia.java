package com.jumia.validator.customer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacksonvieira
 *
 */
public class ValidatorEthiopia implements ValidatorPhoneNumber {

	private static String regex = "\\(251\\)\\ ?[1-59]\\d{8}$";

	@Override
	public Boolean validate(String phoneNumber) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.find();
	}

}
