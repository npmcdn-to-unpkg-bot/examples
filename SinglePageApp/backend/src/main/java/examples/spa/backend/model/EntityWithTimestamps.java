package examples.spa.backend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class EntityWithTimestamps<ID extends Serializable> implements EntityWithId<ID> {

	@Column
	Date createdOn;

	@Column
	Date lastModified;
	
	@PrePersist
	protected void prePersist() {
		this.createdOn = this.lastModified = new Date();
	}
	
	@PreUpdate
	protected void preUpdate() {
		this.lastModified = new Date();
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}
