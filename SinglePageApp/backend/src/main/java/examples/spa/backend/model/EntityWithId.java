package examples.spa.backend.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;

@Access(AccessType.FIELD)
public interface EntityWithId<ID extends Serializable> extends Serializable {
	ID getId();

	void setId(ID id);

	default boolean isNew() {
		return (getId() == null);
	}
}
