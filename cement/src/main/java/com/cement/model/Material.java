package com.cement.model;

import java.io.Serializable;

public class Material implements Serializable {

	int mate_id;
	int type_id;
	int size_id;
	int reg_id;
	int geo_id;
	int supp_id;
	String comment;
	
	public int getMate_id() {
		return mate_id;
	}
	public void setMate_id(int mate_id) {
		this.mate_id = mate_id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public int getSize_id() {
		return size_id;
	}
	public void setSize_id(int size_id) {
		this.size_id = size_id;
	}
	public int getReg_id() {
		return reg_id;
	}
	public void setReg_id(int reg_id) {
		this.reg_id = reg_id;
	}
	public int getGeo_id() {
		return geo_id;
	}
	public void setGeo_id(int geo_id) {
		this.geo_id = geo_id;
	}
	public int getSupp_id() {
		return supp_id;
	}
	public void setSupp_id(int supp_id) {
		this.supp_id = supp_id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
