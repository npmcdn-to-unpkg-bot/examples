package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Access(AccessType.FIELD)
public class SieveValue {

	@Id
	@Column(name="sieve_id")
	private int sieveId;

	@Column(name="sieve")
	private String sieve;

	@Column(name="value")
	private double value;

	public SieveValue() {
	}
	
	public SieveValue(int sieveId, String sieve, double value) {
		this.sieveId = sieveId;
		this.sieve = sieve;
		this.value = value;
	}
	
	public int getSieveId() {
		return sieveId;
	}

	public void setSieveId(int id) {
		this.sieveId = id;
	}

	public String getSieve() {
		return sieve;
	}

	public void setSieve(String sieve) {
		this.sieve = sieve;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return "S:" + sieve + ", V:" + value;
	}
}
