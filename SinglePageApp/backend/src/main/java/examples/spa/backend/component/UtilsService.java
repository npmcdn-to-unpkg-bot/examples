package examples.spa.backend.component;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import examples.spa.backend.misc.FilterItemHelper;
import examples.spa.backend.model.ErrorResponse;

@Service
public class UtilsService {
	@PersistenceContext
	protected EntityManager em;

	@Autowired
	protected MessageSource messageSource;
	
	ConcurrentMap<Class, FilterItemHelper> filterItemHelpers = new ConcurrentHashMap<>();
	
	public <T> FilterItemHelper<T> getFilterItemHelper(Class<T> clazz) {
		return filterItemHelpers.computeIfAbsent(clazz, klazz -> new FilterItemHelper<>(em, klazz));
	}

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
}
