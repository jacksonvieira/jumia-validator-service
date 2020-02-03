package com.jumia.validator.lib.validation;

import com.jumia.validator.lib.service.JumiaException;

/**
 * @author jacksonvieira
 *
 */
public class JumiaViolationException extends JumiaException {

	private static final long serialVersionUID = 7373646313766564424L;

	public JumiaViolationException(String violations) {
		super(violations);
	}

}
