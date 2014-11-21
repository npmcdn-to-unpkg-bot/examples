package com.cement.misc;

import java.util.ArrayList;
import java.util.List;

public class SievePassForm {

	private List<Double> values = new ArrayList<>();

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public String toString() {
		return values == null ? "null" : values.toString();
	}
}
