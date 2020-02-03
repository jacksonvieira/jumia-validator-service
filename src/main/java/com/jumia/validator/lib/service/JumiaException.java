package com.jumia.validator.lib.service;

/**
 * @author jacksonvieira
 *
 */
public class JumiaException extends Exception {
	private static final long serialVersionUID = 1L;

	public JumiaException(String message) {
		super(message);
	}

	public JumiaException(Throwable cause) {
		super(cause);
	}

	public JumiaException(String message, Throwable cause) {
		super(message, cause);
	}
}
