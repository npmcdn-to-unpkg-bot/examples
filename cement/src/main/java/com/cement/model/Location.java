package com.cement.model;

import java.io.Serializable;

public class Location implements Serializable {

	int geo_id;
	String geo_name;
	
	public int getGeo_id() {
		return geo_id;
	}
	public void setGeo_id(int geo_id) {
		this.geo_id = geo_id;
	}
	public String getGeo_name() {
		return geo_name;
	}
	public void setGeo_name(String geo_name) {
		this.geo_name = geo_name;
	}
}
