package examples.spa.backend.misc;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import examples.spa.backend.model.ErrorResponse;

public class Utils {
	public static ResponseEntity makeErrorResponse(BindingResult result, MessageSource ms) {
		Locale locale = LocaleContextHolder.getLocale();
		ErrorResponse errorResponse = new ErrorResponse();
		for (ObjectError i : result.getAllErrors()) {
			String message = ms.getMessage(i, locale);
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
}
