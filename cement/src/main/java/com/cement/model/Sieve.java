package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sieve")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="sieve_id"))
})
@Access(AccessType.FIELD)
public class Sieve extends EntityWithId {

	@Column(name="sieve_d", nullable=false)
	double name;

	public double getName() {
		return name;
	}

	public void setName(double name) {
		this.name = name;
	}
}
