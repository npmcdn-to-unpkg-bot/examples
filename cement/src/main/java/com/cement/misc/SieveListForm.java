package com.cement.misc;

import java.util.List;

public class SieveListForm {

	private List<SieveValue> sieve;

	public List<SieveValue> getSieve() {
		return sieve;
	}

	public void setSieve(List<SieveValue> sieve) {
		this.sieve = sieve;
	}
	
	public String toString() {
		return sieve == null ? "null" : sieve.toString();
	}
}
