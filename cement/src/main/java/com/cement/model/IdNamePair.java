package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Access(AccessType.FIELD)
public class IdNamePair extends EntityWithId {
	@Column(nullable=false)
	String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
