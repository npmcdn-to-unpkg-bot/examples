package examples.spa.backend.model;

import java.io.Serializable;

import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

public interface EntityWithId<ID extends Serializable> extends Serializable {
	@OrderBy
	ID getId();

	void setId(ID id);

	@Transient
	@XmlTransient
	default boolean isNew() {
		return (getId() == null);
	}
}
