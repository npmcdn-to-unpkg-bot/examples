package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "material_sample_sieve_pass")
@Access(AccessType.FIELD)
public class SievePassJustIds implements Serializable {
	@Id
	@Column(name="sample_id", nullable=false)
	int sample;
	
	@Id
	@Column(name="sieve_id", nullable=false)
	int sieve;
	
	@Id
	@Column(name="pass_id", nullable=false)
	int passId;
	
	@Column(name="value", nullable=false)
	double value;

	public SievePassJustIds() {
	}
	
	public SievePassJustIds(int sample, int sieve, int passId, double value) {
		this.sample = sample;
		this.sieve = sieve;
		this.passId = passId;
		this.value = value;
	}
	
	public int getSample() {
		return sample;
	}

	public void setSample(int sample) {
		this.sample = sample;
	}

	public int getSieve() {
		return sieve;
	}

	public void setSieve(int sieve) {
		this.sieve = sieve;
	}

	public int getPassId() {
		return passId;
	}

	public void setPassId(int passId) {
		this.passId = passId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
