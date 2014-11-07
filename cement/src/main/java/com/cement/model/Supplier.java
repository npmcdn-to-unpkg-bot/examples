package com.cement.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mate_supplier")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="supp_id")),
	@AttributeOverride(name="name", column=@Column(name="supp_name"))
})
public class Supplier extends IdNamePair {
}
