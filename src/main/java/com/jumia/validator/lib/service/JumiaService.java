package com.jumia.validator.lib.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumia.validator.lib.validation.Violation;

/**
 * @author jacksonvieira
 *
 */
public abstract class JumiaService<T> {

	public abstract List<T> all();

	public abstract T find(Long id);

	public abstract T save(T t) throws JumiaException;

	public abstract void delete(Long id);

	public void validate(Object bean) throws JumiaException {
		validate(bean, Default.class);
	}

	public void validate(Object bean, Class<?>... classes) throws JumiaException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		throwsViolations(validator.validate(bean, classes));
	}

	private void throwsViolations(Set<ConstraintViolation<Object>> violations) throws JumiaException {
		if (violations.isEmpty()) {
			return;
		}
		String message = formatMessage(violations);
		throw new JumiaException(message);
	}

	private String formatMessage(Set<ConstraintViolation<Object>> violations) throws JumiaException {
		List<Violation> msgViolations = new ArrayList<>();
		for (ConstraintViolation<Object> violation : violations) {
			msgViolations.add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
		}
		ObjectMapper mapper = new ObjectMapper();
		String messages;
		try {
			messages = mapper.writeValueAsString(msgViolations);
		} catch (IOException e) {
			throw new JumiaException(e);
		}
		return messages;
	}
}
