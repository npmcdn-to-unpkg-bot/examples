package com.cement.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "material")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="mate_id"))
})
@Access(AccessType.FIELD)
public class Material extends EntityWithId {
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
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="material")
	Collection<Sample> samples = new ArrayList<Sample>();
	
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

	public Collection<Sample> getSamples() {
		return samples;
	}
}
