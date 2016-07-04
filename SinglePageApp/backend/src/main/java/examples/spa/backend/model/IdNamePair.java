package examples.spa.backend.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import examples.spa.backend.model.FieldDef.Order;

@MappedSuperclass
@Access(AccessType.FIELD)
public class IdNamePair implements EntityWithId<Integer> {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false, updatable = false)
	Integer id;
	
	@Column
	Date createdOn;

	@Column
	Date lastModified;
	
	@Column(nullable=false)
	@OrderBy
	@FieldDef(searchable=true, order=Order.ASC)
	String name;

	public IdNamePair() {
	}
	
	public IdNamePair(Integer id, String name) {
		setId(id);
		this.name = name;
	}

	public IdNamePair(Integer id, double name) {
		setId(id);
		this.name = Double.toString(name);
	}
	
	@PrePersist
	protected void prePersist() {
		this.createdOn = this.lastModified = new Date();
	}
	
	@PreUpdate
	protected void preUpdate() {
		this.lastModified = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public String toString() {
		return getClass().getSimpleName() + " [id:" + getId() + ", name:" + getName() + "]";
	}
}
