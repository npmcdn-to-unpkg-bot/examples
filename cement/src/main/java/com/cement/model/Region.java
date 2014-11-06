package com.cement.model;

import java.io.Serializable;

public class Region implements Serializable {
	
	int reg_id;
	String reg_name;

	public int getReg_id() {
		return reg_id;
	}
	public void setReg_id(int reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_name() {
		return reg_name;
	}
	public void setReg_name(String reg_name) {
		this.reg_name = reg_name;
	}
}
