package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "curve")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="curve_id")),
	@AttributeOverride(name="name", column=@Column(name="curve_name"))
})
public class Curve extends IdNamePair {
}
