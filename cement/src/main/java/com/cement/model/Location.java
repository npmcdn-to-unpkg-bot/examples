package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mate_geo")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="geo_id")),
	@AttributeOverride(name="name", column=@Column(name="geo_name"))
})
public class Location extends IdNamePair {
}
