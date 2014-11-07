package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mate_size")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="size_id")),
	@AttributeOverride(name="name", column=@Column(name="size_name"))
})
public class MaterialSize extends IdNamePair {
}
