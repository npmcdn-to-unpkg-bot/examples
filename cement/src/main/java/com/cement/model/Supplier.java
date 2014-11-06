package com.cement.model;

import java.io.Serializable;

public class Supplier implements Serializable {
	
	int supp_id;
	String supp_name;

	public int getSupp_id() {
		return supp_id;
	}
	public void setSupp_id(int supp_id) {
		this.supp_id = supp_id;
	}
	public String getSupp_name() {
		return supp_name;
	}
	public void setSupp_name(String supp_name) {
		this.supp_name = supp_name;
	}
}
