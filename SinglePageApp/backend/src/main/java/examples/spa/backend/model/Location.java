package examples.spa.backend.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "loc")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="loc_id")),
	@AttributeOverride(name="name", column=@Column(name="loc_name"))
})
@Access(AccessType.PROPERTY)
public class Location extends IdNamePair {
	
	private int dum = this.hashCode();

	@Column(name = "dumint")
	@FieldDef(appendWildcards = false, ignoreCase = false)
	public int getDummy() {
		return dum;
	}

	public void setDummy(int dummy) {
		this.dum = dummy;
	}
}
