package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

@MappedSuperclass
@Access(AccessType.FIELD)
public class IdNamePair extends EntityWithId {
	@Column(nullable=false)
	String name;

	public IdNamePair() {
	}
	
	public IdNamePair(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public IdNamePair(int id, double name) {
		this.id = id;
		this.name = Double.toString(name);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "[id:" + getId() + ", name:" + getName() + "]";
	}
}
