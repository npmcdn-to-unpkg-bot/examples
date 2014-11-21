package com.cement.misc;

import java.util.Map;

import com.cement.model.SieveValue;

public class SieveMapForm {

	private Map<Integer, SieveValue> sieve;

	public Map<Integer, SieveValue> getSieve() {
		return sieve;
	}

	public void setSieve(Map<Integer, SieveValue> sieve) {
		this.sieve = sieve;
	}

	public String toString() {
		return sieve == null ? "null" : sieve.toString();
	}
}
