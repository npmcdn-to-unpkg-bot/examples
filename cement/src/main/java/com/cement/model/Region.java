package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mate_reg")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="reg_id")),
	@AttributeOverride(name="name", column=@Column(name="reg_name"))
})
public class Region extends IdNamePair {
}
