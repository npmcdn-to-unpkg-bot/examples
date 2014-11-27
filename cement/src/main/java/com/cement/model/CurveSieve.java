package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "curve_sieve")
@Access(AccessType.FIELD)
public class CurveSieve implements Serializable {
	@Id
	@Column(name="curve_id", nullable=false)
	int curve;
	
	@Id
	@Column(name="sieve_id", nullable=false)
	int sieve;
	
	@Column(name="low_value", nullable=false)
	double lowValue;

	@Column(name="upper_value", nullable=false)
	double upperValue;

	@Column(name="value", nullable=false)
	double value;

	public CurveSieve() {
	}

	public int getCurve() {
		return curve;
	}

	public void setCurve(int curve) {
		this.curve = curve;
	}

	public int getSieve() {
		return sieve;
	}

	public void setSieve(int sieve) {
		this.sieve = sieve;
	}

	public double getLowValue() {
		return lowValue;
	}

	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}

	public double getUpperValue() {
		return upperValue;
	}

	public void setUpperValue(double upperValue) {
		this.upperValue = upperValue;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CurveSieve [curve=" + curve + ", sieve=" + sieve
				+ ", lowValue=" + lowValue + ", upperValue=" + upperValue
				+ ", value=" + value + "]";
	}
	
}
