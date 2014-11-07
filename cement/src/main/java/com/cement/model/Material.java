package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material")
@Access(AccessType.FIELD)
public class Material implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="mate_id")
	int id;

	@ManyToOne
	@JoinColumn(name="type_id", nullable=false)
	MaterialType materialType;
	
	@ManyToOne
	@JoinColumn(name="size_id", nullable=false)
	MaterialSize materialSize;
	
	@ManyToOne
	@JoinColumn(name="reg_id", nullable=false)
	Region region;
	
	@ManyToOne
	@JoinColumn(name="geo_id", nullable=false)
	Location location;
	
	@ManyToOne
	@JoinColumn(name="supp_id", nullable=false)
	Supplier supplier;
	
	String comment;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public MaterialType getMaterialType() {
		return materialType;
	}
	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
	public MaterialSize getMaterialSize() {
		return materialSize;
	}
	public void setMaterialSize(MaterialSize materialSize) {
		this.materialSize = materialSize;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location Location) {
		this.location = Location;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
