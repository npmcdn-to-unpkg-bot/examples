package com.cement.model;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "recpwiz_set")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="set_id")),
	@AttributeOverride(name="name", column=@Column(name="set_name"))
})
public class ReceiptSet extends IdNamePair {
/*	@OneToMany(fetch=FetchType.LAZY, mappedBy="receiptSet")
	Collection<ReceiptSetMaterial> items;
*/
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="recpwiz_set_mate",
		//foreignKey=@ForeignKey(name="recpwiz_set_fk", value=ConstraintMode.CONSTRAINT),
		//inverseForeignKey=@ForeignKey(name="recpwiz_mate_fk", value=ConstraintMode.CONSTRAINT),
		joinColumns={ @JoinColumn(name="set_id") },
		inverseJoinColumns={ @JoinColumn(name="mate_id") }
	)
	Collection<Material> materials;

	public Collection<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Collection<Material> materials) {
		this.materials = materials;
	}
}
