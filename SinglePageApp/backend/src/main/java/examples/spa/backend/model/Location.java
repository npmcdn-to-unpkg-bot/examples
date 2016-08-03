package examples.spa.backend.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Access(AccessType.PROPERTY)
public class Location extends IdNamePair {
	
	private int dum = this.hashCode();
	
	String email;

	LocationType type = LocationType.Unknown;
	
	@Column
	@FieldDef(searchable = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "dumint")
	@FieldDef(appendWildcards = false, ignoreCase = false)
	public int getDummy() {
		return dum;
	}

	public void setDummy(int dummy) {
		this.dum = dummy;
	}

	public LocationType getType() {
		return type;
	}

	public void setType(LocationType type) {
		this.type = type;
	}
}
