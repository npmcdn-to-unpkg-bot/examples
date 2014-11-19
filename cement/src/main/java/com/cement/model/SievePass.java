package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material_sample_sieve_pass")
@Access(AccessType.FIELD)
public class SievePass implements Serializable {
	@Id
	@ManyToOne
	@JoinColumn(name="sample_id", nullable=false)
	Sample sample;
	
	@Id
	@ManyToOne
	@JoinColumn(name="sieve_id", nullable=false)
	Sieve sieve;
	
	@Id
	@Column(name="pass_id", nullable=false)
	int passId;
	
	@Column(name="value", nullable=false)
	double value;

	public SievePass() {
	}
	
	public SievePass(Sample sample, Sieve sieve, int passId, double value) {
		this.sample = sample;
		this.sieve = sieve;
		this.passId = passId;
		this.value = value;
	}
	
	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public Sieve getSieve() {
		return sieve;
	}

	public void setSieve(Sieve sieve) {
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
