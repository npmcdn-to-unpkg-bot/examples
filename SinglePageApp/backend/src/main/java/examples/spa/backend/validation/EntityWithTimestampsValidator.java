package examples.spa.backend.validation;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import examples.spa.backend.component.EntityWithIdJpa;
import examples.spa.backend.model.EntityWithTimestamps;

public class EntityWithTimestampsValidator<ID extends Serializable, T extends EntityWithTimestamps<ID>> implements Validator {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	EntityWithIdJpa<ID, T> jpa;
	
	public EntityWithTimestampsValidator(EntityWithIdJpa<ID, T> jpa) {
		this.jpa = jpa;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return EntityWithTimestamps.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		if (o == null)
			return;
		T t = (T) o;
		ID id = t.getId();
		if (id != null) {
			Date lastModified = t.getLastModified();
			T t2;
			try {
				t2 = jpa.load(id);
			} catch (Exception ex) {
				log.error("Failed to load item with ID=" + id, ex);
				return;
			}
			Date lastModified2 = t2.getLastModified();
			if (lastModified2 != null) {
				if (lastModified == null) {
					e.reject("lastModified.canNotBeEmptyWhenSavingData");
				} else {
					if (lastModified != lastModified2) {
						e.reject("lastModified.recordHasBeenChanged");
					}
				}
			}
		}
	}
}
