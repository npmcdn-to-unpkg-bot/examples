package com.cement.misc;

public class SieveValue {
	
	private String sieve;
	
	private double value;
	
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
