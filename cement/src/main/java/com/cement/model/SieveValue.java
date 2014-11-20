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

	@Column(name="val")
	private double val;

	public SieveValue() {
	}
	
	public SieveValue(int sieveId, String sieve, double val) {
		this.sieveId = sieveId;
		this.sieve = sieve;
		this.val = val;
	}

	public SieveValue(int sieveId, double sieve, double val) {
		this(sieveId, Double.toString(sieve), val);
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

	public double getVal() {
		return val;
	}

	public void setVal(double value) {
		this.val = value;
	}
	
	public String toString() {
		return "S:" + sieve + ", V:" + val + ", id:" + sieveId;
	}
}
