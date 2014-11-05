package com.slavi.examples.spring.data;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MyDataValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MyData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
		MyData myData = (MyData) target;
		if (myData.getId() < 1)
			errors.rejectValue("id", "negative_value", "Negative value not allowed");
	}

}
