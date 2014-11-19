package com.cement.misc;

import java.util.HashMap;
import java.util.Map;

public class StringMapForm {

	private Map<String, String> map = new HashMap<>();

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	public String toString() {
		return map == null ? "null" : map.toString();
	}
}
