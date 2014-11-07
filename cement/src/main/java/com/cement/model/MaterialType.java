package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mate_type")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="type_id")),
	@AttributeOverride(name="name", column=@Column(name="type_name"))
})
public class MaterialType extends IdNamePair {
}
