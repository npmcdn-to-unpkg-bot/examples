package com.cement.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Entity
//@Table(name = "recpwiz_set_mate")
//@Access(AccessType.FIELD)
public class ReceiptSetMaterial implements Serializable {
	@Id
	@ManyToOne
	@JoinColumn(name="set_id", nullable=false)
	ReceiptSet receiptSet;
	
	@Id
	@ManyToOne
	@JoinColumn(name="mate_id", nullable=false)
	Material material;

	public ReceiptSet getReceiptSet() {
		return receiptSet;
	}

	public void setReceiptSet(ReceiptSet receiptSet) {
		this.receiptSet = receiptSet;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
}
