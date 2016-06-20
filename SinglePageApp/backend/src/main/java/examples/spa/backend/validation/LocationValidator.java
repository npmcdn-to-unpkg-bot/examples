package examples.spa.backend.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import examples.spa.backend.model.Location;

public class LocationValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return Location.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		Location l = (Location) o;
		ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
		if (l.getName().toUpperCase().startsWith("ZZ")) {
			e.rejectValue("name", "name.beginsWithZZ");
			e.rejectValue("name", "name.oneMoreError");
		}
	}
}
