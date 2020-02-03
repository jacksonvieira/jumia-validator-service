package com.jumia.validator.lib.validation;

/**
 * @author jacksonvieira
 *
 */
public class Violation {
	private String property;
	private String message;

	public Violation(String property, String message) {
		this.setProperty(property);
		this.setMessage(message);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
