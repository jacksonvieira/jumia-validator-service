package com.jumia.validator.customer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jacksonvieira
 *
 */
public class ValidatorMozambique implements ValidatorPhoneNumber {

	private static String regex = "\\(258\\)\\ ?[28]\\d{7,8}$";

	@Override
	public Boolean validate(String phoneNumber) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.find();
	}

}
