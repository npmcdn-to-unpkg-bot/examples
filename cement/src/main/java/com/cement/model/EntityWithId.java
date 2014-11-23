package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class EntityWithId implements Serializable {
	@Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false, updatable = false)
	Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNew() {
		return (id == null);
	}
}
