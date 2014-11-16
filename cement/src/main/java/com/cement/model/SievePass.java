package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material_sample_sieve_pass")
@Access(AccessType.FIELD)
public class SievePass implements Serializable {
	@Id
	@ManyToOne
	@JoinColumn(name="sample_id", nullable=false)
	Sample sample;
	
	@Id
	@Column(name="sieve_id", nullable=false)
	int sieveId;
	
	@Id
	@Column(name="pass_id", nullable=false)
	int passId;
	
	@Column(name="value", nullable=false)
	int value;
}
