package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material_sample")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="sample_id"))
})
@Access(AccessType.FIELD)
public class Sample extends EntityWithId {

	@ManyToOne
	@JoinColumn(name="mate_id", nullable=false)
	Material material;
	
	@Column(name="sample_place")
	String samplePlace;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="sample_date")
	String sampleDate;
	
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sample_date_mesr")
	String dateMeasure;
	
	@Column(name="sample_passnumb")
	int passNumber;
	
	@Column(name="sample_status")
	int status;
	
	Double density;
	
	Double weight;

/*	@Column(nullable=true, precision=2)
	Double field1;
	
	@Column(nullable=true, precision=2)
	Double field2;
*/	
	@Column(name="module_size")
	Double moduleSize;

	@Column(name="sample_comment")
	String comment;
	
/*	@OneToMany(fetch=FetchType.LAZY, mappedBy="sample")
	@JoinColumn(name="sample_id")
	Collection<SievePass> sievePasses;
*/
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

	public String getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(String sampleDate) {
		this.sampleDate = sampleDate;
	}

	public String getDateMeasure() {
		return dateMeasure;
	}

	public void setDateMeasure(String dateMeasure) {
		this.dateMeasure = dateMeasure;
	}

	public int getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(int passNumber) {
		this.passNumber = passNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Double getDensity() {
		return density;
	}

	public void setDensity(Double density) {
		this.density = density;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

/*	public Double getField1() {
		return field1;
	}

	public void setField1(Double field1) {
		this.field1 = field1;
	}

	public Double getField2() {
		return field2;
	}

	public void setField2(Double field2) {
		this.field2 = field2;
	}
*/
	public Double getModuleSize() {
		return moduleSize;
	}

	public void setModuleSize(Double moduleSize) {
		this.moduleSize = moduleSize;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

/*	public Collection<SievePass> getSievePasses() {
		return sievePasses;
	}
*/
}
