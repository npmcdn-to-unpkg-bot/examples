package com.cement.model;

import java.io.Serializable;
import java.util.Date;

public class Sample implements Serializable {

	int sample_id;
	int mate_id;
	String sample_place;
	Date sample_date;
	Date sample_date_mesr;
	int sample_passnumb;
	int sample_satus;
	double density;
	double weight;
	String filed1, field2;
	double module_size;
	String sample_content;
	
	public int getSample_id() {
		return sample_id;
	}
	public void setSample_id(int sample_id) {
		this.sample_id = sample_id;
	}
	public int getMate_id() {
		return mate_id;
	}
	public void setMate_id(int mate_id) {
		this.mate_id = mate_id;
	}
	public String getSample_place() {
		return sample_place;
	}
	public void setSample_place(String sample_place) {
		this.sample_place = sample_place;
	}
	public Date getSample_date() {
		return sample_date;
	}
	public void setSample_date(Date sample_date) {
		this.sample_date = sample_date;
	}
	public Date getSample_date_mesr() {
		return sample_date_mesr;
	}
	public void setSample_date_mesr(Date sample_date_mesr) {
		this.sample_date_mesr = sample_date_mesr;
	}
	public int getSample_passnumb() {
		return sample_passnumb;
	}
	public void setSample_passnumb(int sample_passnumb) {
		this.sample_passnumb = sample_passnumb;
	}
	public int getSample_satus() {
		return sample_satus;
	}
	public void setSample_satus(int sample_satus) {
		this.sample_satus = sample_satus;
	}
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getFiled1() {
		return filed1;
	}
	public void setFiled1(String filed1) {
		this.filed1 = filed1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public double getModule_size() {
		return module_size;
	}
	public void setModule_size(double module_size) {
		this.module_size = module_size;
	}
	public String getSample_content() {
		return sample_content;
	}
	public void setSample_content(String sample_content) {
		this.sample_content = sample_content;
	}
	
}
