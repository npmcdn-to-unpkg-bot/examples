package examples.spa.backend.component;

import java.util.Locale;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import examples.spa.backend.model.ErrorResponse;

@Service
public class UtilsService {
	@Autowired
	protected MessageSource messageSource;
	
	public ResponseEntity makeErrorResponse(BindingResult result) {
		Locale locale = LocaleContextHolder.getLocale();
		ErrorResponse errorResponse = new ErrorResponse();
		for (ObjectError i : result.getAllErrors()) {
			String message = messageSource.getMessage(i, locale);
			String field;
			if (i instanceof FieldError) {
				field = ((FieldError) i).getField();
			} else {
				field = null; // i.getObjectName();
			}
			errorResponse.getList(field).add(message);
		}
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
	}
	
	public static String objectToString(Object o) {
		return ReflectionToStringBuilder.toString(o, ToStringStyle.SHORT_PREFIX_STYLE);	
	}
	
	public static <T> T nvl(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}
}
