package com.cement.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "material_sample")
@Access(AccessType.FIELD)
public class Sample implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sample_id")
	int id;
	
	@ManyToOne
	@JoinColumn(name="mate_id", nullable=false)
	Material material;
	
	@Column(name="sample_place")
	String samplePlace;
	
	@Temporal(TemporalType.DATE)
	@Column(name="sample_date")
	Date sampleDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="sample_date_mesr")
	Date dateMeasure;
	
	@Column(name="sample_passnumb")
	int passNumber;
	
	@Column(name="sample_satus")
	int satus;
	
	double density;
	
	double weight;

	String filed1;
	
	String field2;
	
	@Column(name="module_size")
	double moduleSize;

	@Column(name="sample_content")
	String sampleContent;
	
/*	@OneToMany(fetch=FetchType.LAZY, mappedBy="sample")
	@JoinColumn(name="sample_id")
	Collection<SievePass> sievePasses;
*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getSamplePlace() {
		return samplePlace;
	}

	public void setSamplePlace(String samplePlace) {
		this.samplePlace = samplePlace;
	}

	public Date getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}

	public Date getDateMeasure() {
		return dateMeasure;
	}

	public void setDateMeasure(Date dateMeasure) {
		this.dateMeasure = dateMeasure;
	}

	public int getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(int passNumber) {
		this.passNumber = passNumber;
	}

	public int getSatus() {
		return satus;
	}

	public void setSatus(int satus) {
		this.satus = satus;
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

	public double getModuleSize() {
		return moduleSize;
	}

	public void setModuleSize(double moduleSize) {
		this.moduleSize = moduleSize;
	}

	public String getSampleContent() {
		return sampleContent;
	}

	public void setSampleContent(String sampleContent) {
		this.sampleContent = sampleContent;
	}

/*	public Collection<SievePass> getSievePasses() {
		return sievePasses;
	}
*/
}
